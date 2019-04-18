package edu.bsu.cs498;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.xml.soap.Text;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainPageController implements Initializable {

    private SpeechRecognizerMain mySpeechRecognizer = new SpeechRecognizerMain();
    private String soundFile = "chime.mp3";
    private MediaPlayer mediaPlayer;

    @FXML Button speechRecBtn;
    @FXML Label statusLabel;
    @FXML Label voiceLabel;
    @FXML MenuItem aboutBtn;
    @FXML GridPane gridPaneList;

    //THE BELOW METHOD IS JUST FOR TESTING, ACTUAL INFO WILL COME FROM PRACTICE SETUP!
    @FXML
    public void addToGridPane() {
        TextField field1 = new TextField();
        TextField field2 = new TextField();
        TextField field3 = new TextField();
        TextField field4 = new TextField();
        TextField field5 = new TextField();
        TextField field6 = new TextField();
        field1.setText("3");
        field2.setText("Chinici");
        field3.setText("7");
        field4.setText("Scannell");
        field5.setText("19");
        field6.setText("Turner");
        gridPaneList.add(field1, 0, 0);
        gridPaneList.add(field2, 1, 0);
        gridPaneList.add(field3, 0, 1);
        gridPaneList.add(field4, 1, 1);
        gridPaneList.add(field5, 0, 2);
        gridPaneList.add(field6, 1, 2);
    }

    @FXML
    private void handleButtonAction() {
        Media sound = new Media(new File(soundFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        if(!mySpeechRecognizer.getSpeechRecognizerThreadRunning()) {
            statusLabel.setText("Loading Speech Recognizer...");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mySpeechRecognizer.SpeechRecognizerMain();
                    speechRecBtn.setStyle("-fx-background-color: Green");
                    speechRecBtn.setText("Speech Recognition");
                    statusLabel.setText("You can start to speak...");
                    mediaPlayer.play();
                }
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
    public int getPlayerRow(int playerNum) {
        String num = Integer.toString(playerNum);
        int row = -1;
        ObservableList<Node> children = gridPaneList.getChildren();

        HBox hb = (HBox)children.get(0);
        System.out.println(hb);
        ObservableList<Node> hbChildren = hb.getChildren();
        TextField tfs = (TextField) hbChildren.get(0);
        System.out.println("tfs = " + tfs);

        System.out.println("getPlayerRow gives: ");
        for (Node node : children) {
            TextField tf = (TextField) node;
            System.out.println(tf.getText());
            if (tf.getText().equals(num)) {
                row = gridPaneList.getRowIndex(node);
            }
        }
        return row;
    }

    @FXML
    private void openInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help Information");
        alert.setHeaderText("Help Info");
        alert.setContentText("On this page, you can track statistics during practice.");
        alert.showAndWait();
    }



    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane statGrid;
    @FXML
    private List<Spinner<Integer>> statSpinners = new ArrayList<>();
    @FXML
    private Button testBtn;
    //private XMLFileHandler handler = new XMLFileHandler();
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
        getSpinnerValues();
    }

    private void getSpinnerValues() {
        int numStats = statNames.size();
        int playerIndex = 0;
        for (int i = playerIndex; i < numStats; i++) {
            String idName = spinnerIDs.get(i);
            int statValue = getSpinner(0, i).getValue();
            //System.out.println(idName + ": " + Integer.toString(statValue));
        }
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
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 16; j++) {
                int initialValue = 0;
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, initialValue);
                //Spinner<Integer> spinner = new Spinner<>(0, 10000, 0, 1);
                Spinner<Integer> spinner = new Spinner<Integer>();
                spinner.setValueFactory(valueFactory);
                //USE THE BELOW COMMENTED OUT CODE TO INCREMENT A SPECIFIED SPINNER!
                //spinner.getValueFactory().increment(1);
                //valueFactory.setValue(0);
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



//    private void setUpMenuBar() {
//
//    }
}
