package edu.bsu.cs498;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpeechRecognizerMainTest {

    SpeechRecognizerMain speechRecognizerMain = new SpeechRecognizerMain();
    MainPageController mainPageController = new MainPageController();

    @Test
    public void makeDecisionForNoWords() throws Exception {
        String speech = "";
        speechRecognizerMain.makeDecision(speech);
    }

    @Test
    public void makeDecisionForSingleWord() throws Exception {
        speechRecognizerMain.makeDecision("one");
    }
}