package edu.bsu.cs498;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainPageController implements Initializable {
    @FXML
    private GridPane playerGrid;
    @FXML
    private GridPane statGrid;
    @FXML
    private List<Spinner<Integer>> statSpinners = new ArrayList<>();
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

    @FXML Button speechRecBtn;
    @FXML Label statusLabel;
    @FXML Label voiceLabel;
    @FXML private ComboBox teamOptions;
    @FXML private ComboBox practiceOptions;
    private ObservableList<String> teamOptionsList = FXCollections.observableArrayList();
    private ObservableList<String> teamPracticesList = FXCollections.observableArrayList();
    private XMLFileHandler handler = new XMLFileHandler();
    private List<String> statNames = Arrays.asList("Kills", "Errors", "Total Attempts", "Assists", "Service Aces", "Service Errors", "Reception Errors", "Digs", "Solo Blocks", "Block Assists", "Blocking Errors", "Ball Handling Errors");
    private HashMap<Integer, String> spinnerIDs = new HashMap<>();
    private SpeechRecognizerMain mySpeechRecognizer = new SpeechRecognizerMain();
    private MediaPlayer mediaPlayer;
    private EnglishStringToNumber stringToNumber = new EnglishStringToNumber();
    private String teamName;
    private String practiceName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenuBar();
        initializeHashMap();
        teamOptionsList = handler.getAllTeams();
        teamOptions.setItems(teamOptionsList);
//        setUpGridPanes();
    }

    private void setUpMenuBar() {
        closeMenuItem.setOnAction(this::closeMenuItemAction);
        genCSVMenuItem.setOnAction(this::genCSVMenuItemAction);
        genAvgCSVMenuItem.setOnAction(this::genAvgCSVMenuItemAction);
        saveStatsMenuItem.setOnAction(this::saveStatsMenuItemAction);
        aboutMenuItem.setOnAction(this::aboutMenuItemAction);
    }

    private void closeMenuItemAction(ActionEvent event) {
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

    private void genCSVMenuItemAction(ActionEvent event) {
        boolean isSuccessful = exportStatistics(false);
        displayGenerationPopup(isSuccessful);
    }

    private void genAvgCSVMenuItemAction(ActionEvent event) {
        boolean isSuccessful = exportStatistics(true);
        displayGenerationPopup(isSuccessful);
    }

    private void displayGenerationPopup(boolean isSuccessful){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Status");
        alert.setHeaderText(null);
        if(isSuccessful){
            alert.setContentText("Spreadsheet was successfully generated");
            alert.showAndWait();
        } else {
            alert.setContentText("There was a problem generating the spreadsheet. Please ensure that the file is not currently being used by another process.");
            alert.showAndWait();
        }
    }

    private void saveStatsMenuItemAction(ActionEvent event) {
        List<Integer> spinnerVals = getSpinnerValues();
        // get both teamName and practiceName programmatically eventually
        String teamName = "Team 1";
        String practiceName = "Practice 1";
        boolean isSuccessful = handler.updatePlayerStats(spinnerVals, teamName, practiceName);
        displaySavePopup(isSuccessful);
    }

    private void displaySavePopup(boolean isSuccessful) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Status");
        alert.setHeaderText(null);
        if(isSuccessful){
            alert.setContentText("Stats have successfully been saved");
            alert.showAndWait();
        } else {
            alert.setContentText("There was a problem saving the stats");
            alert.showAndWait();
        }
    }

    private void aboutMenuItemAction(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("PracStat version 1.0");
        alert.setContentText("Created by W.E.B. Enterprises");
        alert.showAndWait();
    }

    private void initializeHashMap() {
        for (int i = 0; i < statNames.size(); i++) {
            spinnerIDs.put(i, statNames.get(i));
        }
    }

    private List<Integer> getSpinnerValues() {
        List<Integer> spinnerVals = new ArrayList<>();
        int numStats = statNames.size();
        List<Player> players = handler.getPlayersByTeamPractice("Team 1", "Practice 1");// teamName and practiceName should be read from mainPage element
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
        // get both teamName and practiceName programmatically eventually
        String teamName = "Team 1";
        String practiceName = "Practice 1";
        // file name should be teamName_practiceName_currentDate
        String fileName = teamName + "_" + practiceName + "_" + LocalDate.now() + ".csv";
        handler.updatePlayerStats(spinnerVals, teamName, practiceName);
        if (showAvg) {
            players = handler.getPlayersInAllPractices(teamName);
            practices = handler.getPracticesByTeam(teamName);
        } else {
            players = handler.getPlayersByTeamPractice(teamName, practiceName);
            practices = Collections.singletonList(practiceName);
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
                Spinner<Integer> spinner = new Spinner<>(0, 10000, stats.get(i).intValue(), 1);
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

    @FXML
    private void handleButtonAction() {
        URL chimeURL = this.getClass().getResource("/sounds/chime.mp3");
        Media sound = new Media(new File(chimeURL.getPath()).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        if(!mySpeechRecognizer.getSpeechRecognizerThreadRunning()) {
            statusLabel.setText("Loading Speech Recognizer...");
            Platform.runLater(() -> {
                mySpeechRecognizer.SpeechRecognizerMain();
                speechRecBtn.setStyle("-fx-background-color: Green");
                speechRecBtn.setText("Speech Recognition");
                statusLabel.setText("You can start to speak...");
                mediaPlayer.play();
            });
        }
        else if(mySpeechRecognizer.getSpeechRecognizerThreadRunning()) {
            if(!mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.ignoreSpeechRecognitionResults();
                System.out.println("ignoring speech recognition results");
                speechRecBtn.setStyle("-fx-background-color: Red");
                statusLabel.setText("Ignoring speech recognition results...");
            }
            else if(mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
                System.out.println("listening to speech recognition results");
                speechRecBtn.setStyle("-fx-background-color: Green");
                statusLabel.setText("Listening to speech recognition results...");
                mediaPlayer.play();
            }
        }
    }

    @FXML
    public int getPlayerRow(String playerNum) {
        int row = -1;
        for (int i = 0; i < playerGrid.getChildren().size(); i++) {
            Node child1 = playerGrid.getChildren().get(i);
            if (child1 instanceof TextField) {
                TextField tfield = (TextField) child1;
                //System.out.println("tfield text = " + tfield.getText());
                if (tfield.getText().length() <=2 && Integer.parseInt(tfield.getText()) == stringToNumber.convert(playerNum)) {
                    row = playerGrid.getRowIndex(child1);
                    //System.out.println("row = " + row);
                }
            }
        }
        return row;
    }

    //Use this method to get a specific spinner and increment it by 1
    public void incrementSpinner(int row, int col) throws InterruptedException {
        Spinner spinner = getSpinner(row, col);
        //getSpinner(row, col).getValueFactory().increment(1);
        spinner.getValueFactory().increment(1);
        //spinner.getStyleClass().clear();
        //spinner.getStyleClass().removeIf(style -> style.equals("spinner incremented tonormal"));
        System.out.println("The OLD style class is: " + spinner.getStyleClass());
        spinner.getStyleClass().add("incremented");
        System.out.println("The NEW style class is: " + spinner.getStyleClass());
        TimeUnit.SECONDS.sleep(1);
        //spinner.getStyleClass().add("tonormal");
        spinner.getStyleClass().remove("incremented");
        System.out.println("The go-back style class is: " + spinner.getStyleClass());
    }

    // Value factory.
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory<Integer>() {
        @Override
        public void decrement(int steps) {
            Integer current = this.getValue();
            int idx = statSpinners.indexOf(current);
            int newIdx = (statSpinners.size() + idx - steps) % statSpinners.size();
            Spinner<Integer> newInt = statSpinners.get(newIdx);
            this.setValue(newInt.getValue());
        }

        @Override
        public void increment(int steps) {
            Integer current = this.getValue();
            int idx = statSpinners.indexOf(current);
            int newIdx = (idx + steps) % statSpinners.size();
            Spinner<Integer> newInt = statSpinners.get(newIdx);
            this.setValue(newInt.getValue());
        }

    };

    public void setVoiceLabelText(String str) {
        voiceLabel.setText(str);
    }

    public void loadPractices(){
        teamPracticesList = handler.getPracticesByTeamObservable(teamOptions.getValue().toString());
        practiceOptions.setItems(teamPracticesList);
    }

    public String getPracticeName(){
        return practiceOptions.getValue().toString();
    }

    public String getTeamName(){
        return teamOptions.getValue().toString();
    }

    @FXML
    private void loadPlayersFromDropdown(){
        teamName = getTeamName();
        practiceName = getPracticeName();
        setUpGridPanes();
    }
}
