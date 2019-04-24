package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;


public class MainPageController{
    @FXML private ComboBox teamOptions;
    @FXML private ComboBox practiceOptions;
    private ObservableList<String> teamOptionsList = FXCollections.observableArrayList();
    private ObservableList<String> teamPracticesList = FXCollections.observableArrayList();
    private XMLFileHandler reader = new XMLFileHandler();

    public void initialize(){
        teamOptionsList = reader.getAllTeams();
        teamOptions.setItems(teamOptionsList);
    }

    public void loadPractices(){
        teamPracticesList = reader.getPracticesByTeamObservable(teamOptions.getValue().toString());
        practiceOptions.setItems(teamPracticesList);
    }

    public String getPracticeName(){
        return practiceOptions.getValue().toString();
    }


}
