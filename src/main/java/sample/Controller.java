package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.SpeechRecognizerMain;
import java.io.File;

public class Controller {

    private SpeechRecognizerMain mySpeechRecognizer = new SpeechRecognizerMain();
    private String soundFile = "chime.mp3";
    private MediaPlayer mediaPlayer;

    @FXML Button speechRecBtn;
    @FXML Label statusLabel;

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

    /**
     * The below code is to be used if we decide we would like to use radio buttons instead of one button to toggle speech recognizer.
     */
/*
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
    */
}
