package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class MainPageController implements Initializable {
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane statGrid;
    @FXML
    private List<Spinner<Integer>> statSpinners = new ArrayList<>();
    @FXML
    private Button testBtn;
    private XMLFileHandler handler = new XMLFileHandler();
    private List<String> players = new ArrayList<>();
    private List<String> statNames = Arrays.asList("Kills", "Errors", "Total Attempts", "Assists", "Service Aces", "Service Errors", "Reception Errors", "Digs", "Solo Blocks", "Block Assists", "Blocking Errors", "Ball Handling Errors");
    private HashMap<Integer,String> spinnerIDs = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        setUpMenuBar();
        initializeHashMap();
        setButtonActions();
        setUpGridPane();
//        readConfigData();
    }

//    private void readConfigData() {
//
//    }


    private void initializeHashMap() {
        for (int i = 0; i < statNames.size(); i++){
            spinnerIDs.put(i, statNames.get(i));
        }
    }

    private void setButtonActions() {
        setTestButtonAction();
    }

    private void setTestButtonAction() {
        testBtn.setOnAction(this::testButtonAction);
    }

    private void testButtonAction(ActionEvent event) {
        generateCSVFile();
    }

    List<Integer> getSpinnerValues() {
        List<Integer> spinnerVals = new ArrayList<>();
        int numStats = statNames.size();
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            for(int j = 0; j < numStats; j++){// there should be 12 stats
                String idName = spinnerIDs.get(j);
                int statValue = getSpinner(i, j).getValue();
                spinnerVals.add(statValue);
//                System.out.println(idName + ": " + Integer.toString(statValue));
            }
//            System.out.println("------------------");
        }
        return spinnerVals;
    }

    private void generateCSVFile(){
        List<Integer> spinnerVals = getSpinnerValues();
        String teamName = "Team 1";
        String practiceName = "Practice 1";
        handler.updatePlayerStats(spinnerVals, teamName, practiceName);

//        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");
//        List<Integer> teamStats = handler.getTeamStats("Team 1");
//        // statNames
//
//        CSVFileMaker csvFileMaker = new CSVFileMaker(spinnerVals, players, statNames, teamName, teamStats, practiceName);
//        //csvFileMaker.generateCSVFile();
//        csvFileMaker.generateCSVFile();
    }

//    private void updateFile(Document doc) {
//        handler.updateXML(doc);
//    }

    private void printSpinnerVals() {
        for (Spinner<Integer> spinner : statSpinners) {
            System.out.println("Value is " + spinner.getValue());
        }
    }

    private void setUpGridPane() {
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
        int numStats = statNames.size();
        int playerCounter = 0;
        for(Player player : players){
            List<Integer> stats = player.getStats();
            for(int i = 0; i < numStats; i++){
                Spinner<Integer> spinner = new Spinner<>(0, 10000, stats.get(i), 1);
                spinner.setPrefWidth(100);
                spinner.setPrefHeight(30);
                statSpinners.add(spinner);
                statGrid.add(spinner, i, playerCounter);
            }
            playerCounter++;
        }
    }

    private Spinner<Integer> getSpinner(int row, int col) {
        for (Node child : statGrid.getChildren()) {
            if (GridPane.getColumnIndex(child) != null && GridPane.getRowIndex(child) != null && GridPane.getColumnIndex(child) == col && GridPane.getRowIndex(child) == row) {
                return (Spinner<Integer>) child;
            }
        }
        return null;
    }

//    private void setUpMenuBar() {
//
//    }
}
