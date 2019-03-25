package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class NewPracticeController {
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> numberColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private ComboBox teamOptionsSelection;
    private ObservableList<Player> playerList = FXCollections.observableArrayList();
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
        System.out.println(teamOptionsSelection.getValue());

    }










}
