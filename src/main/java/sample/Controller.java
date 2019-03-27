package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.SpeechRecognizerMain;

public class Controller {

    private boolean firstRun = true;
    private SpeechRecognizerMain mySpeechRecognizer;

    @FXML
    Button speechRecBtn;

    @FXML
    private void handleButtonAction() {
        if(firstRun) {
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
            speechRecBtn.setStyle("-fx-background-color: Green");
            speechRecBtn.setText("Speech Recognition");
        }
        else if(!firstRun) {
            if(!mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.ignoreSpeechRecognitionResults();
                System.out.println("ignoring speech recognition results");
                speechRecBtn.setStyle("-fx-background-color: Red");
                //speechRecBtn.setBackground(javafx.scene.paint.Color.color(green));
                //speechRecBtn.setBackground(Color.red);
            }
            else if(mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
                System.out.println("listening to speech recognition results");
                //speechRecBtn.setBackground(Color.green);
                speechRecBtn.setStyle("-fx-background-color: Green");
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
