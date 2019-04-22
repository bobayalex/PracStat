package edu.bsu.cs498;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.application.Platform;

import static edu.bsu.cs498.StartPageController.mainPageController1;

public class SpeechRecognizerMain {

    // Necessary
    private LiveSpeechRecognizer recognizer;
    EnglishStringToNumber stringToNumber	= new EnglishStringToNumber();

    // Logger
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * This String contains the Result that is coming back from SpeechRecognizer
     */
    private String speechRecognitionResult;

    //-----------------Lock Variables-----------------------------

    /**
     * This variable is used to ignore the results of speech recognition cause actually it can't be stopped...
     *
     * <br>
     * Check this link for more information: <a href=
     * "https://sourceforge.net/p/cmusphinx/discussion/sphinx4/thread/3875fc39/">https://sourceforge.net/p/cmusphinx/discussion/sphinx4/thread/3875fc39/</a>
     */
    private boolean ignoreSpeechRecognitionResults = false;

    /**
     * Checks if the speech recognise is already running
     */
    private boolean speechRecognizerThreadRunning = false;

    /**
     * Checks if the resources Thread is already running
     */
    private boolean resourcesThreadRunning;

    //---

    /**
     * This executor service is used in order the playerState events to be executed in an order
     */
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);

    //------------------------------------------------------------------------------------

    /**
     * Constructor
     */
    public void SpeechRecognizerMain() {

        // Loading Message
        logger.log(Level.INFO, "Loading Speech Recognizer...\n");

        // Configuration
        Configuration configuration = new Configuration();

        // Load model from the jar
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

        //====================================================================================
        //=====================READ THIS!!!===============================================
        //Uncomment this line of code if you want the recognizer to recognize every word of the language
        //you are using , here it is English for example
        //====================================================================================
        //configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        //====================================================================================
        //=====================READ THIS!!!===============================================
        //If you don't want to use a grammar file comment below 3 lines and uncomment the above line for language model
        //====================================================================================

        // Grammar
        configuration.setGrammarPath("resource:/grammars");
        configuration.setGrammarName("grammar");
        configuration.setUseGrammar(true);

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        // Start recognition process pruning previously cached data.
        // recognizer.startRecognition(true);

        //Check if needed resources are available
        startResourcesThread();
        //Start speech recognition thread
        startSpeechRecognition();
    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Starts the Speech Recognition Thread
     */
    public synchronized void startSpeechRecognition() {

        //Check lock
        if (speechRecognizerThreadRunning)
            logger.log(Level.INFO, "Speech Recognition Thread already running...\n");
        else
            //Submit to ExecutorService
            eventsExecutorService.submit(() -> {
                //locks
                speechRecognizerThreadRunning = true;
                ignoreSpeechRecognitionResults = false;

                //Start Recognition
                recognizer.startRecognition(true);

                //Information
                logger.log(Level.INFO, "You can start to speak...\n");

                try {
                    while (speechRecognizerThreadRunning) {
						/*
						 * This method will return when the end of speech is reached. Note that the end pointer will determine the end of speech.
						 */
                        SpeechResult speechResult = recognizer.getResult();

                        //Check if we ignore the speech recognition results
                        if (!ignoreSpeechRecognitionResults) {

                            //Check the result
                            if (speechResult == null)
                                logger.log(Level.INFO, "I can't understand what you said.\n");
                            else {

                                //Get the hypothesis
                                speechRecognitionResult = speechResult.getHypothesis();

                                //You said?
                                System.out.println("You said: [" + speechRecognitionResult + "]\n");

                                //Call the appropriate method
                                makeDecision(speechRecognitionResult);

                            }
                        } else
                            logger.log(Level.INFO, "Ingoring Speech Recognition Results...");

                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                    speechRecognizerThreadRunning = false;
                }

                logger.log(Level.INFO, "SpeechThread has exited...");

            });
    }

    /**
     * Stops ignoring the results of SpeechRecognition
     */
    public synchronized void stopIgnoreSpeechRecognitionResults() {

        //Stop ignoring speech recognition results
        ignoreSpeechRecognitionResults = false;
    }

    /**
     * Ignores the results of SpeechRecognition
     */
    public synchronized void ignoreSpeechRecognitionResults() {

        //Instead of stopping the speech recognition we are ignoring it's results
        ignoreSpeechRecognitionResults = true;

    }

    //-----------------------------------------------------------------------------------------------

    /**
     * Starting a Thread that checks if the resources needed to the SpeechRecognition library are available
     */
    public void startResourcesThread() {

        //Check lock
        if (resourcesThreadRunning)
            logger.log(Level.INFO, "Resources Thread already running...\n");
        else
            //Submit to ExecutorService
            eventsExecutorService.submit(() -> {
                try {

                    //Lock
                    resourcesThreadRunning = true;

                    // Detect if the microphone is available
                    while (true) {

                        //Is the Microphone Available
                        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE))
                            logger.log(Level.INFO, "Microphone is not available.\n");

                        // Sleep some period
                        Thread.sleep(350);
                    }

                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                    resourcesThreadRunning = false;
                }
            });
    }

    public void makeDecision(String speech) throws InterruptedException {

        // Initialize playerNum and stat
        int playerNum = -1;
        String stat = "No Stat";

        // Split the sentence
        String[] array = speech.split(" ");

        System.out.println("array length = " + array.length);

        // return if user said only one word
        if (array.length == 1) {
            System.out.println("User only said one word (or less)!");
            updateVoiceRecStatus("User only said one word (or less)!");
            return;
        }
        else if (array.length == 2) {
            playerNum = stringToNumber.convert(array[0]);
            System.out.println(mainPageController1.getPlayerRow(array[0]));
            stat = array[1];
            if (playerNum == -1) {
                System.out.println("User didn't say a player number to start command!");
                updateVoiceRecStatus("User didn't say a player number to start command!");
            }
        }
        else if (array.length == 3) {
            playerNum = stringToNumber.convert(array[0]);
            System.out.println(mainPageController1.getPlayerRow(array[0]));
            stat = array[1] + " " + array[2];
            if (playerNum == -1) {
                System.out.println("User didn't say a player number to start command!");
                updateVoiceRecStatus("User didn't say a player number to start command!");
            }
        }
        else {
            System.out.println("User said too many words!");
            updateVoiceRecStatus("User said too many words!");
            return;
        }

        System.out.println("The playerNumber int = " + playerNum);
        System.out.println("The stat String = " + stat);
        System.out.println("The user said: " + speech);
        updateVoiceRecStatus("The user said: " + speech);
        if (getStatColumn(stat) > -1 && playerNum > -1 && mainPageController1.getPlayerRow(array[0]) > -1) {
            mainPageController1.incrementSpinner(mainPageController1.getPlayerRow(array[0]), getStatColumn(stat));
            if (getStatColumn(stat) == 0 || getStatColumn(stat) == 1) {
                mainPageController1.incrementSpinner(mainPageController1.getPlayerRow(array[0]), 2);
            }
        }
    }

    private void updateVoiceRecStatus(String str) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                mainPageController1.setVoiceLabelText(str);
            }
        });
    }

    private int getStatColumn(String stat) {
        if (stat.equals("kill")) {
            return 0;
        }
        else if (stat.equals("attack error")) {
            return 1;
        }
        else if (stat.equals("attack")) {
            return 2;
        }
        else if (stat.equals("assist")) {
            return 3;
        }
        else if (stat.equals("ace") || stat.equals("service ace") || stat.equals("serve ace") || stat.equals("ace serve")) {
            return 4;
        }
        else if (stat.equals("service error") || stat.equals("serve error")) {
            return 5;
        }
        else if (stat.equals("receive error")) {
            return 6;
        }
        else if (stat.equals("dig")) {
            return 7;
        }
        else if (stat.equals("solo block") || stat.equals("block")) {
            return 8;
        }
        else if (stat.equals("block assist")) {
            return 9;
        }
        else if (stat.equals("block error")) {
            return 10;
        }
        else if (stat.equals("lift") || stat.equals("double") || stat.equals("double contact")) {
            return 11;
        }
        else {
            System.out.println("Spoken stat not a valid voice command!");
            return -1;
        }
    }

    public boolean getIgnoreSpeechRecognitionResults() {
        return ignoreSpeechRecognitionResults;
    }

    public boolean getSpeechRecognizerThreadRunning() {
        return speechRecognizerThreadRunning;
    }

    /**
     * Main Method
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpeechRecognizerMain();
    }
}