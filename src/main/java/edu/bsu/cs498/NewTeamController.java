package edu.bsu.cs498;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class NewTeamController {
    private ObservableList<Player> playerList = FXCollections.observableArrayList();
    private int playerCount = 1;
    @FXML private TextField teamNameInput;
    @FXML private TextField playerNameInput;
    @FXML private TextField playerNumberInput;
    @FXML private TextArea output;
    @FXML private Button mainMenuButton;
    @FXML private Button createTeamButton;
    @FXML private ComboBox positionOptions;
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;

    public void initialize(){
        XMLFileHandler reader = new XMLFileHandler();
        setButtonActions();
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void addPlayer(){
        if (playerInfoEntered()){
            playerTable.setItems(playerList);
            playerList.add(new Player(playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString()));
            output.setText("Player " + playerCount + " Added");
            playerCount++;
            playerNameInput.clear();
            playerNumberInput.clear();
            positionOptions.setValue(null);
        } else {popupMessage("Error", "Error adding player");}
    }

    private void createTeamButtonAction(ActionEvent event){
        if (teamInfoEntered()){
            try {Team newTeam = new Team(teamNameInput.getText(), playerList);
                popupMessage("Message",newTeam.getTeamName() + " has been created.");
            }catch (IllegalArgumentException e){popupMessage("Error", "Team already exists");}
        } else {popupMessage("Error","Error adding team");}
    }

    private boolean playerInfoEntered(){
        if (playerNameInput.getText().equals("") | playerNumberInput.getText().equals("")){
            return false;
        } return true;
    }

    private boolean teamInfoEntered(){
        if (teamNameInput.getText().equals("")){
            return false;
        } return true;
    }
    private void setButtonActions() {
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
        createTeamButton.setOnAction(this::createTeamButtonAction);
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {
            Parent updatedRoot = FXMLLoader.load(getClass().getResource("/fxml/menuPage.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(updatedRoot);
        } catch (IOException e) {e.printStackTrace();}
    }

    private void popupMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PracStat");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

