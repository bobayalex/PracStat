package edu.bsu.cs498;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ViewPracticeController implements Initializable {
    @FXML
    private Label teamNameLabel;
    @FXML
    private Label practiceNameLabel;
    @FXML
    private Button mainMenuButton;
    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane statGrid;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem genCSVMenuItem;
    @FXML
    private MenuItem genAvgCSVMenuItem;
    @FXML
    private MenuItem saveStatsMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    Label statusLabel;
    @FXML
    Label voiceLabel;
    @FXML
    private ComboBox teamOptions;
    @FXML
    private ComboBox practiceOptions;
    private XMLFileHandler handler = new XMLFileHandler();
    private List<String> statNames = Arrays.asList("Kills", "Errors", "Total Attempts", "Assists", "Service Aces", "Service Errors", "Reception Errors", "Digs", "Solo Blocks", "Block Assists", "Blocking Errors", "Ball Handling Errors");
    private String teamName;
    private String practiceName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenuBar();
        ObservableList<String> teamOptionsList = handler.getAllTeams();
        teamOptions.setItems(teamOptionsList);
        mainMenuButton.setOnAction(this::mainMenuButtonAction);
    }

    private void mainMenuButtonAction(ActionEvent event) {
        try {
            switchRoot(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpMenuBar() {
        closeMenuItem.setOnAction(event -> closeMenuItemAction());
        genCSVMenuItem.setOnAction(event -> genCSVMenuItemAction());
        genAvgCSVMenuItem.setOnAction(event -> genAvgCSVMenuItemAction());
        saveStatsMenuItem.setOnAction(event -> saveStatsMenuItemAction());
        aboutMenuItem.setOnAction(event -> aboutMenuItemAction());
    }

    private void closeMenuItemAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit PracStat?");
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            alert.close();
        } else if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else if (result.get() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    private void genCSVMenuItemAction() {
        boolean isSuccessful = exportStatistics(false);
        displayGenerationPopup(isSuccessful);
    }

    private void genAvgCSVMenuItemAction() {
        boolean isSuccessful = exportStatistics(true);
        displayGenerationPopup(isSuccessful);
    }

    private void displayGenerationPopup(boolean isSuccessful) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Status");
        alert.setHeaderText(null);
        if (isSuccessful) {
            alert.setContentText("Spreadsheet was successfully generated. It can be found in your documents folder.");
            alert.showAndWait();
        } else {
            alert.setContentText("There was a problem generating the spreadsheet. Please ensure that the file is not currently being used by another process.");
            alert.showAndWait();
        }
    }

    private void saveStatsMenuItemAction() {
        List<Integer> spinnerVals = getSpinnerValues();
        boolean isSuccessful = handler.updatePlayerStats(spinnerVals, teamName, practiceName);
        displaySavePopup(isSuccessful);
    }

    private void displaySavePopup(boolean isSuccessful) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Status");
        alert.setHeaderText(null);
        if (isSuccessful) {
            alert.setContentText("Stats have successfully been saved");
            alert.showAndWait();
        } else {
            alert.setContentText("There was a problem saving the stats");
            alert.showAndWait();
        }
    }

    private void aboutMenuItemAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("PracStat version 1.0");
        alert.setContentText("Created by W.E.B. Enterprises");
        alert.showAndWait();
    }

    private List<Integer> getSpinnerValues() {
        List<Integer> spinnerVals = new ArrayList<>();
        int numStats = statNames.size();
        List<Player> players = handler.getPlayersByTeamPractice(teamName, practiceName);
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 0; j < numStats; j++) {// there should be 12 stats
                int statValue = Objects.requireNonNull(getSpinner(i, j)).getValue();
                spinnerVals.add(statValue);
            }
        }
        return spinnerVals;
    }

    private boolean exportStatistics(boolean showAvg) {
        List<Player> players;
        List<String> practices;
        List<Integer> spinnerVals = getSpinnerValues();

        String fileName;
        handler.updatePlayerStats(spinnerVals, teamName, practiceName);
        if (showAvg) {
            players = handler.getPlayersInAllPractices(teamName);
            practices = handler.getPracticesByTeam(teamName);
            fileName = teamName + "_" + practiceName + "_Average_" + LocalDate.now() + ".csv";

        } else {
            players = handler.getPlayersByTeamPractice(teamName, practiceName);
            practices = Collections.singletonList(practiceName);
            fileName = teamName + "_" + practiceName + "_" + LocalDate.now() + ".csv";
        }
        List<Double> teamStats = handler.getTeamStats(teamName);
        CSVFileMaker csvFileMaker = new CSVFileMaker(players, teamName, teamStats, practices);
        String outputPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        outputPath += "\\PracStat_Spreadsheets";
        return csvFileMaker.generateCSVFile(fileName, outputPath);
    }

    private void setUpGridPanes() {
        setUpPlayerGrid();
        setUpStatGrid();
    }

    private void setUpPlayerGrid() {
        List<Player> players = handler.getPlayersByTeamPractice(teamName, practiceName);// teamName and practiceName should be read from mainPage element
        TextField numberField;
        TextField nameField;
        for (int i = 0; i < players.size(); i++) {
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
        List<Player> players = handler.getPlayersByTeamPractice(teamName, practiceName);// teamName and practiceName should be read from mainPage element
        int numStats = statNames.size(); // should be 12
        int playerCounter = 0;
        for (Player player : players) {
            List<Double> stats = player.getStats();
            for (int i = 0; i < numStats; i++) {
                Spinner<Integer> spinner = new Spinner<>(stats.get(i).intValue(), stats.get(i).intValue(), stats.get(i).intValue(), 1);
                spinner.setPrefWidth(100);
                spinner.setPrefHeight(30);
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

    public void loadPractices() {
        ObservableList<String> teamPracticesList = handler.getPracticesByTeamObservable(teamOptions.getValue().toString());
        practiceOptions.setItems(teamPracticesList);
    }

    private String getPracticeName() {
        if (practiceOptions.getValue() == null) {
            return "";
        }
        return practiceOptions.getValue().toString();
    }

    private String getTeamName() {
        if (teamOptions.getValue() == null) {
            return "";
        }
        return teamOptions.getValue().toString();
    }

    @FXML
    private void loadPlayersFromDropdown() {
        teamName = getTeamName();
        practiceName = getPracticeName();
        if (!teamName.equals("") && !practiceName.equals("")) {
            playerGrid.getChildren().clear();
            statGrid.getChildren().clear();
            teamNameLabel.setText(teamName);
            practiceNameLabel.setText(practiceName);
            setUpGridPanes();
        }
    }

    private void switchRoot(ActionEvent event) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource("/fxml/menuPage.fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}
