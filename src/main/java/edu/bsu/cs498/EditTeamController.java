package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ListIterator;

public class EditTeamController {
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private ComboBox teamOptionsSelection;
    @FXML private ComboBox playerSelection;
    @FXML private TextField playerNameInput;
    @FXML private TextField playerNumberInput;
    private ObservableList<String> teamOptions = FXCollections.observableArrayList();
    private ObservableList<String> playerOptions = FXCollections.observableArrayList();
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private ObservableList<Player> playerObjectList = FXCollections.observableArrayList();
    private String[] playerInfo;
    private XMLFileHandler reader = new XMLFileHandler();

    public void initialize(){
        teamOptions = reader.getAllTeams();
        teamOptionsSelection.setItems(teamOptions);
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void loadPlayerTable(){
        playerObjectList.clear();
        playerOptions.clear();
        playerStringList = reader.getTeamPlayers(teamOptionsSelection.getValue().toString());
        ListIterator<String> teamIterator = playerStringList.listIterator();
        while (teamIterator.hasNext()){
            playerInfo = teamIterator.next().split(",");
            String playerName = playerInfo[0];
            String playerNumber = playerInfo[1];
            String playerPosition = playerInfo[2];
            playerOptions.add(playerName);

            Player player = new Player(playerName, playerNumber, playerPosition);
            playerObjectList.add(player);
        }
        playerSelection.setItems(playerOptions);
        playerTable.setItems(playerObjectList);
    }

}
