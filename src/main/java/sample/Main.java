package sample;

import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.HashMap;
import java.util.Map;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 575, 500));
        primaryStage.show();

        //Configuration configuration = new Configuration();

        //configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        //configuration.setDictionaryPath("resource:/fxml/pracstatdict.dic"); //"resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict"
        //configuration.setLanguageModelPath("resource:/fxml/pracstatlangm.lm"); //"resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin"

        //configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        //configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");


        //LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        // Start recognition process pruning previously cached data.
        //recognizer.startRecognition(true);
        /*
        while(true) {
            System.out.println("started recognizing...");
            System.out.println("say: seven block, twelve ace, etc.");

            String utterance = recognizer.getResult().getHypothesis();

            if (utterance.startsWith("exit"))
                break;
            if (utterance.startsWith("seven")) {
                recognizer.stopRecognition();
                System.out.println("recognized that utterance started with BLOCK!!!");
                recognizer.startRecognition(true);
            }
        }

        //System.out.println("started recognition");
        while(true){

            SpeechResult result = recognizer.getResult();

            System.out.println(result.getHypothesis());

        }
        //SpeechResult result;


        // Pause recognition process. It can be resumed then with startRecognition(false).
        //recognizer.stopRecognition();


        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();
        System.out.println("stopped recognition");

*/
    }



    public static void main(String[] args) {
        launch(args);
    }
}