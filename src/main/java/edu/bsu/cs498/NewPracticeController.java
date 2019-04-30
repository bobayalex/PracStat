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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class NewPracticeController {
    @FXML
    private VBox playerListVBox;
    @FXML
    private ComboBox teamOptionsSelection;
    @FXML
    private Button newTeamButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button createPracticeButton;
    @FXML
    private Button continuePracticeButton;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField practiceNameInput;
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private String[] playerInfo;
    private XMLFileHandler reader = new XMLFileHandler();
    static MainPageController mainPageController1;

    public void initialize() {
        setButtonActions();
        ObservableList<String> teamOptions = reader.getAllTeams();
        teamOptionsSelection.setItems(teamOptions);
    }

    public void loadPlayerTable() {
        playerStringList.clear();
        playerListVBox.getChildren().clear();
        playerStringList = reader.getAllPlayersString(teamOptionsSelection.getValue().toString());
        for (String playerStr : playerStringList) {
            playerInfo = playerStr.split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            CheckBox cbox = new CheckBox(playerName + "," + playerNumber + "," + playerPosition);
            cbox.setId(playerName + "," + playerNumber + "," + playerPosition);
            cbox.setSelected(true);
            playerListVBox.getChildren().add(cbox);
        }
        scroll.setContent(playerListVBox);
    }


    private void setButtonActions() {
        newTeamButton.setOnAction(this::newTeamButtonAction);
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
        createPracticeButton.setOnAction(this::createPracticeButtonAction);
        continuePracticeButton.setOnAction(this::continuePracticeButtonAction);
    }

    private void newTeamButtonAction(ActionEvent event) {
        try {
            switchRoot(event, "/fxml/newTeam.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {
            switchRoot(event, "/fxml/menuPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPracticeButtonAction(ActionEvent event) {
        if (missingInfo()) {
            popupMessage("Please Enter All Necessary Information");
            return;
        }
        if (getSelectedPlayers().size() == 0) {
            popupMessage("At least 1 player must be selected to create a practice");
            return;
        }
        String practiceName = practiceNameInput.getText();
        if (practiceName.contains("/") || practiceName.contains("\\")) {
            popupMessage("Practice name cannot contain forward or backward slashes");
            return;
        }
        ObservableList<PlayerData> selectedPlayersData = getSelectedPlayers();
        reader.createPractice(teamOptionsSelection.getValue().toString(), practiceName, selectedPlayersData);
        try {
            switchRootMainCont(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continuePracticeButtonAction(ActionEvent event) {
        try {
            switchRootMainCont(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<PlayerData> getSelectedPlayers() {
        ObservableList<PlayerData> selectedPlayersData = FXCollections.observableArrayList();
        for (Node child : playerListVBox.getChildren()) {
            if (child instanceof CheckBox) {
                if (((CheckBox) child).isSelected()) {
                    playerInfo = child.getId().split(",");
                    String playerName = playerInfo[0];
                    String playerNumber = playerInfo[1];
                    String playerPosition = playerInfo[2];
                    PlayerData playerData = new PlayerData(playerName, playerNumber, playerPosition);
                    selectedPlayersData.add(playerData);
                }
            }
        }
        return selectedPlayersData;
    }

    private void switchRootMainCont(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
        Parent updatedRoot = loader.load();
        mainPageController1 = loader.getController();
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.getStylesheets().add("css/mainpage.css");
        Stage currentStage = (Stage) currentScene.getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }

    private void popupMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PracStat");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean missingInfo() {
        return practiceNameInput.getText().length() == 0 || teamOptionsSelection.getValue() == null;
    }
}