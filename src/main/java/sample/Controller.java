package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.SpeechRecognizerMain;

public class Controller {

    private boolean firstRun = true;
    private SpeechRecognizerMain mySpeechRecognizer;

    @FXML Button speechRecBtn;
    @FXML Label statusLabel;

    @FXML
    private void handleButtonAction(){
        if(firstRun) {
            statusLabel.setText("Loading Speech Recognizer...");
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
            speechRecBtn.setStyle("-fx-background-color: Green");
            speechRecBtn.setText("Speech Recognition");
            statusLabel.setText("You can start to speak...");
        }
        else if(!firstRun) {
            if(!mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.ignoreSpeechRecognitionResults();
                System.out.println("ignoring speech recognition results");
                speechRecBtn.setStyle("-fx-background-color: Red");
                statusLabel.setText("Ignoring speech recognition results...");
                //speechRecBtn.setBackground(javafx.scene.paint.Color.color(green));
                //speechRecBtn.setBackground(Color.red);
            }
            else if(mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
                System.out.println("listening to speech recognition results");
                //speechRecBtn.setBackground(Color.green);
                speechRecBtn.setStyle("-fx-background-color: Green");
                statusLabel.setText("Listening to speech recognition results...");
            }
        }
    }

    @FXML
    private void handleSpeechRecOnBtn() {
        if(firstRun) {
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
        }
        else if(!firstRun) {
            mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
            System.out.println("listening to speech recognition results");
        }
    }

    @FXML
    private void handleSpeechRecOffBtn() {
        mySpeechRecognizer.ignoreSpeechRecognitionResults();
        System.out.println("ignoring speech recognition results");
    }
}
