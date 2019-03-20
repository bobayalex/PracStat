package edu.bsu.cs498;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

public class NewTeamController {
    private ObservableList<Player> playerList = FXCollections.observableArrayList();
    private int playerCount = 1;
    @FXML private TextField teamNameInput;
    @FXML private TextField playerNameInput;
    @FXML private TextField playerNumberInput;
    @FXML private TextArea output;
    @FXML private ComboBox positionOptions;
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;

    public void tableConfig(){
        nameColumn.setCellValueFactory(new PropertyValueFactory("playerName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory("playerNumber"));
        positionColumn.setCellValueFactory(new PropertyValueFactory("playerPosition"));
    }

    public void addPlayer(){
        tableConfig();
        playerTable.setItems(playerList);
        if (playerNameInput == null || playerNumberInput == null){
            System.out.println("Error");
            return;
        }
        playerList.add(new Player(playerNameInput.getText(), playerNumberInput.getText(), positionOptions.getValue().toString()));
        output.setText("Player " + playerCount + " Added");
        playerCount++;
        playerNameInput.clear();
        playerNumberInput.clear();
    }

    public void createTeam(){
        Team newTeam = new Team(teamNameInput.getText(), playerList);
        output.setText(newTeam.getTeamInfo());
    }

    public ObservableList<Player> getPlayerList(){
        return playerList;
    }

}

