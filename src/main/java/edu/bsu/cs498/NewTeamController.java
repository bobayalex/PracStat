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
    private ObservableList<PlayerData> playerDataList = FXCollections.observableArrayList();
    private int playerCount = 1;
    @FXML private TextField teamNameInput;
    @FXML private TextField playerNameInput;
    @FXML private TextField playerNumberInput;
    @FXML private TextArea output;
    @FXML private Button mainMenuButton;
    @FXML private Button createTeamButton;
    @FXML private ComboBox positionOptions;
    @FXML private TableView<PlayerData> playerDataTable;
    @FXML private TableColumn<PlayerData, String> nameColumn;
    @FXML private TableColumn<PlayerData, String> numberColumn;
    @FXML private TableColumn<PlayerData, String> positionColumn;

    public void initialize(){
        setButtonActions();
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void addPlayer(){
        try{
            if (Integer.parseInt(playerNumberInput.getText()) > 79){
            popupMessage("Error", "Player Number must be less than 80");
            return;
            }
            if (playerNameInput.getText().length() < 3){
                popupMessage("Error", "Player Name must be at least 3 characters");
                return;
            }
            if (playerInfoEntered()){
                playerDataTable.setItems(playerDataList);
                playerDataList.add(new PlayerData(playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString()));
                output.setText("Player " + playerCount + " Added");
                playerCount++;
                playerNameInput.clear();
                playerNumberInput.clear();
                positionOptions.setValue(null);
            } else {popupMessage("Error", "Error adding player");}
        } catch (NumberFormatException e){}
    }

    private void createTeamButtonAction(ActionEvent event){
        if (teamInfoEntered()){
            try {Team newTeam = new Team(teamNameInput.getText(), playerDataList);
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

