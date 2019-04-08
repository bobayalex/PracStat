package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ListIterator;

public class NewPracticeController {
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private ComboBox teamOptionsSelection;
    @FXML private Button newTeamButton;
    @FXML private Button mainMenuButton;
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private ObservableList<Player> playerObjectList = FXCollections.observableArrayList();
    private String[] playerInfo;
    ObservableList<String> teamOptions;
    private XMLFileHandler reader = new XMLFileHandler();

    public void initialize(){
        setButtonActions();
        teamOptions = reader.getAllTeams();
        teamOptionsSelection.setItems(teamOptions);
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void loadPlayerTable(){
        playerObjectList.clear();
        playerStringList = reader.getAllPlayersString(teamOptionsSelection.getValue().toString());
        ListIterator<String> teamIterator = playerStringList.listIterator();
        while (teamIterator.hasNext()){
            playerInfo = teamIterator.next().split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            Player player = new Player(playerName, playerNumber, playerPosition);
            playerObjectList.add(player);
        }
        playerTable.setItems(playerObjectList);
    }

    private void setButtonActions() {
        newTeamButton.setOnAction(this::newTeamButtonAction);
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
    }

    private void newTeamButtonAction(ActionEvent event) {
        try {switchRoot(event, "/fxml/newTeam.fxml");}
        catch (IOException e) {e.printStackTrace();}
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {switchRoot(event, "/fxml/menuPage.fxml");}
        catch (IOException e) {e.printStackTrace();}
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}

