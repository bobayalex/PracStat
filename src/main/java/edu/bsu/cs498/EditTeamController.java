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
    @FXML private Button mainMenuButton;
    @FXML private TableView<Player> playerTable;
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
    private ObservableList<Player> playerObjectList = FXCollections.observableArrayList();
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
        playerObjectList.clear();
        playerStringList.clear();
        playerOptionsList.clear();
        playerStringList = reader.getAllPlayersString(teamOptions.getValue().toString());
        ListIterator<String> teamIterator = playerStringList.listIterator();
        while (teamIterator.hasNext()){
            playerInfo = teamIterator.next().split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            playerOptionsList.add(playerName + "," + playerNumber + "," + playerPosition);
            Player player = new Player(playerName, playerNumber, playerPosition);
            playerObjectList.add(player);
        }
        playerSelection.setItems(playerOptionsList);
        playerTable.setItems(playerObjectList);
    }

    public void selectPlayerToEdit(){
        if (playerSelection.getValue() != null){
            playerStringList.clear();
            playerObjectList.clear();
            playerStringList.add(playerSelection.getValue().toString());
            playerInfo = playerStringList.get(0).split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            Player player = new Player(playerName, playerNumber, playerPosition);
            playerObjectList.add(player);
            playerTable.setItems(playerObjectList);
        }
    }

    public void editPlayer(){
        String playerInfo = playerSelection.getValue().toString();
        String[] playerInfoSplit = playerInfo.split(",");
        String playerName = playerInfoSplit[0];
        reader.editPlayer(playerName, playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString());
        refreshPage();
    }

    public void addNewPlayer(){
        Player newPlayer = new Player(playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString());
        reader.addPlayer(teamOptions.getValue().toString(), newPlayer);
        refreshPage();
    }

    public void deletePlayer(){
        playerStringList.add(playerSelection.getValue().toString());
        playerInfo = playerStringList.get(0).split(",");
        String playerName = playerInfo[0];
        reader.deletePlayer(playerName);
        refreshPage();
    }

    public void refreshPage(){
        playerNameInput.clear();
        playerNumberInput.clear();
        loadPlayerTable();
    }

    private void setButtonActions() {
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {
            Parent updatedRoot = FXMLLoader.load(getClass().getResource("/fxml/menuPage.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(updatedRoot);
        } catch (IOException e) {e.printStackTrace();}
    }

}
