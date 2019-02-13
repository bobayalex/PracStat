package sample;

import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 575, 500));
        primaryStage.show();



        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/fxml/pracstatdict.dic"); //"resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict"
        configuration.setLanguageModelPath("resource:/fxml/pracstatlangm.lm"); //"resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin"

        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        // Start recognition process pruning previously cached data.
        recognizer.startRecognition(true);
        System.out.println("started recognition");
        SpeechResult result = recognizer.getResult();
        // Pause recognition process. It can be resumed then with startRecognition(false).
        //recognizer.stopRecognition();


        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
        System.out.println("stopped recognition");


    }


    public static void main(String[] args) {
        launch(args);
    }
}
