package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ListIterator;

public class EditTeamController {
    @FXML private Button submitEditButton;
    @FXML private Button mainMenuButton;
    @FXML private Button addPlayerButton;
    @FXML private Button deletePlayerButton;
    @FXML private TableView<PlayerData> playerDataTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private ComboBox teamOptions;
    @FXML private ComboBox playerSelection;
    @FXML private ComboBox positionOptions;
    @FXML private TextField playerNameInput;
    @FXML private TextField playerNumberInput;
    private ObservableList<String> teamOptionsList = FXCollections.observableArrayList();
    private ObservableList<String> playerOptionsList = FXCollections.observableArrayList();
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private ObservableList<PlayerData> playerDataObjectList = FXCollections.observableArrayList();
    private String[] playerInfo;
    private XMLFileHandler reader = new XMLFileHandler();

    public void initialize(){
        setButtonActions();
        teamOptionsList = reader.getAllTeams();
        teamOptions.setItems(teamOptionsList);
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void loadPlayerTable(){
        playerDataObjectList.clear();
        playerStringList.clear();
        playerOptionsList.clear();
        playerStringList = reader.getAllPlayersString(teamOptions.getValue().toString());
        for (String aPlayerStringList : playerStringList) {
            playerInfo = aPlayerStringList.split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            playerOptionsList.add(playerName + "," + playerNumber + "," + playerPosition);
            PlayerData playerData = new PlayerData(playerName, playerNumber, playerPosition);
            playerDataObjectList.add(playerData);
        }
        playerSelection.setItems(playerOptionsList);
        playerDataTable.setItems(playerDataObjectList);
    }

    public void selectPlayerToEdit(){
        if (playerSelection.getValue() != null){
            playerStringList.clear();
            playerDataObjectList.clear();
            playerStringList.add(playerSelection.getValue().toString());
            playerInfo = playerStringList.get(0).split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            PlayerData playerData = new PlayerData(playerName, playerNumber, playerPosition);
            playerDataObjectList.add(playerData);
            playerDataTable.setItems(playerDataObjectList);
        }
    }

    private boolean isAPlayerSelected(){
        return playerSelection.getValue() != null;
    }

    private boolean missingInfo() {
        return playerNameInput.getText().length() == 0 || playerNumberInput.getText().length() == 0 || positionOptions.getValue() == null;
    }

    public void editPlayer(){
        if (Integer.parseInt(playerNumberInput.getText()) > 100){
            popupMessage("Error", "Player Number must be 100 or below");
            return;
        }
        if (playerNameInput.getText().length() < 3){
            popupMessage("Error", "Player Name must be at least 3 characters");
            return;
        }
        if (!missingInfo() && isAPlayerSelected()){
            String playerInfo = playerSelection.getValue().toString();
            String[] playerInfoSplit = playerInfo.split(",");
            String playerName = playerInfoSplit[0];
            reader.editPlayer(playerName, playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString());
            refreshPage();}
        else{popupMessage("Error", "Error editing player. Please make sure all necessary information has been entered.");}
    }

    public void addNewPlayer(){
        if (!missingInfo()) {
            if (Integer.parseInt(playerNumberInput.getText()) > 79) {
                popupMessage("Error", "Player Number must be less than 80");
            }
            else {
                PlayerData newPlayerData = new PlayerData(playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString());
                reader.addPlayer(teamOptions.getValue().toString(), newPlayerData, reader.getTeamPlayersNode(reader.getTeamNode(teamOptions.getValue().toString())), true);
                refreshPage();
            }
        } else {popupMessage("Error", "Error adding player. Please make sure all necessary information has been entered.");}
    }

    public void deletePlayer(){
        if (playerSelection.getValue() == null){
            popupMessage("Error", "Please select a player to delete");
        }else{
            playerStringList.add(playerSelection.getValue().toString());
            playerInfo = playerStringList.get(0).split(",");
            String playerName = playerInfo[0];
            reader.deletePlayer(playerName);
            refreshPage();}
    }

    private void refreshPage(){
        playerNameInput.clear();
        playerNumberInput.clear();
        positionOptions.setValue(null);
        loadPlayerTable();
    }

    private void popupMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PracStat");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setButtonActions() {
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
        submitEditButton.setOnAction(this::submitEditButtonAction);
        addPlayerButton.setOnAction(this::addPlayerButtonAction);
        deletePlayerButton.setOnAction(this::deletePlayerButtonAction);
    }

    private void deletePlayerButtonAction(ActionEvent event) {
        deletePlayer();
    }

    private void addPlayerButtonAction(ActionEvent event) {
        addNewPlayer();
    }

    private void submitEditButtonAction(ActionEvent event) {
        editPlayer();
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {
            Parent updatedRoot = FXMLLoader.load(getClass().getResource("/fxml/menuPage.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(updatedRoot);
        } catch (IOException e) {e.printStackTrace();}
    }

}
