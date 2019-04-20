package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.*;

public class MainPageController implements Initializable {
    @FXML
    private GridPane playerGrid;
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane statGrid;
    @FXML
    private List<Spinner<Integer>> statSpinners = new ArrayList<>();
    @FXML
    private Button testBtn;
    private XMLFileHandler handler = new XMLFileHandler();
    private List<String> statNames = Arrays.asList("Kills", "Errors", "Total Attempts", "Assists", "Service Aces", "Service Errors", "Reception Errors", "Digs", "Solo Blocks", "Block Assists", "Blocking Errors", "Ball Handling Errors");
    private HashMap<Integer,String> spinnerIDs = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        setUpMenuBar();
        initializeHashMap();
        setButtonActions();
        setUpGridPanes();
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
        exportStatistics(false);
    }

    private List<Integer> getSpinnerValues() {
        List<Integer> spinnerVals = new ArrayList<>();
        int numStats = statNames.size();
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            for(int j = 0; j < numStats; j++){// there should be 12 stats
                int statValue = Objects.requireNonNull(getSpinner(i, j)).getValue();
                spinnerVals.add(statValue);
            }
        }
        return spinnerVals;
    }

    private void exportStatistics(boolean showAvg){
        List<String> practices;
        List<Integer> spinnerVals = getSpinnerValues();
        String teamName = "Team 1";
        String practiceName = "Practice 1";
        if(showAvg){
            practices = handler.getPracticesByTeam("Team 1");
        } else {
            practices = Collections.singletonList(practiceName);
        }
        handler.updatePlayerStats(spinnerVals, teamName, practiceName);
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");
        List<Integer> teamStats = handler.getTeamStats("Team 1");

        CSVFileMaker csvFileMaker = new CSVFileMaker(players, teamName, teamStats, practices);
        csvFileMaker.generateCSVFile("testCSVFile.csv");
    }

//    private void updateFile(Document doc) {
//        handler.updateXML(doc);
//    }

    private void setUpGridPanes(){
        setUpPlayerGrid();
        setUpStatGrid();
    }

    private void setUpPlayerGrid() {
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
        TextField numberField;
        TextField nameField;
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            int number = player.getNumber();
            numberField = new TextField(String.valueOf(number));
            numberField.setEditable(false);

            String name = player.getName();
            nameField = new TextField(name);
            nameField.setEditable(false);
            playerGrid.add(numberField, 0, i);
            playerGrid.add(nameField, 1, i);
        }
    }

    private void setUpStatGrid() {
        // column constraints are for stats, row constraints are for players
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
        int numStats = statNames.size(); // should be 12
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
