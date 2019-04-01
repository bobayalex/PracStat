package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ListIterator;

public class NewPracticeController {
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private ComboBox teamOptionsSelection;
    private ObservableList<String> playerStringList = FXCollections.observableArrayList();
    private ObservableList<Player> playerObjectList = FXCollections.observableArrayList();

    private String[] playerInfo;
    ObservableList<String> teamOptions;
    private XMLFileReader reader = new XMLFileReader();


    public void initialize(){
        reader.printXML();
        teamOptions = reader.getAllTeams();
        teamOptionsSelection.setItems(teamOptions);
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void loadPlayerTable(){
        playerObjectList.clear();
        playerStringList = reader.getTeamPlayers(teamOptionsSelection.getValue().toString());
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
}

