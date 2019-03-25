package sample;

import javafx.fxml.FXML;
import model.SpeechRecognizerMain;

public class Controller {

    private boolean firstRun = true;
    private boolean recognitionStarted = false;
    private SpeechRecognizerMain mySpeechRecognizer;

    @FXML
    private void handleButtonAction() {
        // button was clicked.. do something
        if(firstRun = true) {
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
            recognitionStarted = true;
        }
        else if(firstRun = false) {
            if(recognitionStarted = true) {
                mySpeechRecognizer.ignoreSpeechRecognitionResults();
            }
            else if(recognitionStarted = false) {
                mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
            }
        }
    }
}
