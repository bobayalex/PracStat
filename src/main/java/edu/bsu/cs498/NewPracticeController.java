package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ListIterator;

public class NewPracticeController {
    @FXML private VBox playerListVBox;
    @FXML private ComboBox teamOptionsSelection;
    @FXML private Button newTeamButton;
    @FXML private Button mainMenuButton;
    @FXML private Button createPracticeButton;
    @FXML private ScrollPane scroll;
    @FXML private TextField practiceNameInput;
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private String[] playerInfo;
    ObservableList<String> teamOptions;
    private XMLFileHandler reader = new XMLFileHandler();

    public void initialize(){
        setButtonActions();
        teamOptions = reader.getAllTeams();
        teamOptionsSelection.setItems(teamOptions);
    }

    public void loadPlayerTable(){
        playerStringList.clear();
        playerListVBox.getChildren().clear();
        playerStringList = reader.getAllPlayersString(teamOptionsSelection.getValue().toString());
        ListIterator<String> teamIterator = playerStringList.listIterator();
        while (teamIterator.hasNext()){
            playerInfo = teamIterator.next().split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            CheckBox cbox = new CheckBox(playerName + "," + playerNumber + "," + playerPosition);
            cbox.setId(playerName + "," + playerNumber + "," + playerPosition);
            playerListVBox.getChildren().add(cbox);
        }
        scroll.setContent(playerListVBox);
    }


    private void setButtonActions() {
        newTeamButton.setOnAction(this::newTeamButtonAction);
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
        createPracticeButton.setOnAction(this::createPracticeButtonAction);
    }

    private void newTeamButtonAction(ActionEvent event) {
        try {switchRoot(event, "/fxml/newTeam.fxml");}
        catch (IOException e) {e.printStackTrace();}
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {switchRoot(event, "/fxml/menuPage.fxml");}
        catch (IOException e) {e.printStackTrace();}
    }

    private void createPracticeButtonAction(ActionEvent event){
        String practiceName = practiceNameInput.getText();
        ObservableList<Player> selectedPlayers = getSelectedPlayers();
        reader.createPractice(teamOptionsSelection.getValue().toString(), practiceName, selectedPlayers);
        try {switchRoot(event, "/fxml/mainPage.fxml");}
        catch (IOException e) {e.printStackTrace();}
    }

    private ObservableList<Player> getSelectedPlayers(){
        ObservableList<Player> selectedPlayers = FXCollections.observableArrayList();
        for (Node child : playerListVBox.getChildren()) {
            if (child instanceof CheckBox){
                if(((CheckBox) child).isSelected()){
                    playerInfo = child.getId().split(",");
                    String playerName = playerInfo[0];
                    String playerNumber = playerInfo[1];
                    String playerPosition = playerInfo[2];
                    Player player = new Player (playerName, playerNumber, playerPosition);
                    selectedPlayers.add(player);
                }
            }
        }
        return selectedPlayers;
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}

