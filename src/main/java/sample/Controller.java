package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.SpeechRecognizerMain;

import java.io.File;

public class Controller {

    private boolean firstRun = true;
    private SpeechRecognizerMain mySpeechRecognizer;
    private String soundFile = "chime.mp3";
    private MediaPlayer mediaPlayer;

    @FXML Button speechRecBtn;
    @FXML Label statusLabel;

    @FXML
    private void handleButtonAction(){
        Media sound = new Media(new File(soundFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        if(firstRun) {
            statusLabel.setText("Loading Speech Recognizer...");
            mySpeechRecognizer = new SpeechRecognizerMain();
            mySpeechRecognizer.SpeechRecognizerMain();
            firstRun = false;
            speechRecBtn.setStyle("-fx-background-color: Green");
            speechRecBtn.setText("Speech Recognition");
            statusLabel.setText("You can start to speak...");
            mediaPlayer.play();
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
                mediaPlayer.play();
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
