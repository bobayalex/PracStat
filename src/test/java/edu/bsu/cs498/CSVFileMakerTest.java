package edu.bsu.cs498;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class CSVFileMakerTest {
    private XMLFileHandler handler = new XMLFileHandler();
    private String fileName = "testCSVFile.csv";

    @Before
    public void setup(){
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");
        List<Integer> teamStats = handler.getTeamStats("Team 1");
        CSVFileMaker csvFileMaker = new CSVFileMaker(players, "Team 1", teamStats, "Practice 1");
        csvFileMaker.generateCSVFile(fileName);
    }

    @Test
    public void test_file_exists(){
        File file = new File(fileName);
        Assert.assertTrue(file.isFile());
    }

    @Test
    public void test_empty_file(){
        File file = new File(fileName);
        Assert.assertNotEquals(0, file.length());
    }

}
