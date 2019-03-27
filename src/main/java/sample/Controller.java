package sample;

import javafx.fxml.FXML;
import model.SpeechRecognizerMain;

public class Controller {

    private boolean firstRun = true;
    private SpeechRecognizerMain mySpeechRecognizer;

    @FXML
    private void handleButtonAction() {
        if(firstRun) {
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
        }
        else if(!firstRun) {
            if(!mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.ignoreSpeechRecognitionResults();
                System.out.println("ignoring speech recognition results");
            }
            else if(mySpeechRecognizer.getIgnoreSpeechRecognitionResults()) {
                mySpeechRecognizer.stopIgnoreSpeechRecognitionResults();
                System.out.println("listening to speech recognition results");
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
