package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import org.w3c.dom.Document;

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
        getPlayerStats();
    }

    private void getPlayerStats() {
        int numStats = statNames.size();
        int playerIndex = 0;
        for (int i = playerIndex; i < numStats; i++) {
            String idName = spinnerIDs.get(i);
            int statValue = getSpinner(0, i).getValue();
            //System.out.println(idName + ": " + Integer.toString(statValue));
        }
    }

    private void updateFile(Document doc) {
        handler.updateXML(doc);
    }

    private void printSpinnerVals() {
        for (Spinner<Integer> spinner : statSpinners) {
            System.out.println("Value is " + spinner.getValue());
        }
    }

    private void setUpGridPane() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 16; j++) {
                Spinner<Integer> spinner = new Spinner<>(0, 10000, 0, 1);
                spinner.setPrefWidth(100);
                spinner.setPrefHeight(30);
                statSpinners.add(spinner);
                statGrid.add(spinner, i, j);
            }
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
