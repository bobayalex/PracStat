package edu.bsu.cs498;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVFileMakerTest {
    private XMLFileHandler handler = new XMLFileHandler();
    private String fileName = "testCSVFile.csv";
    private CSVFileMaker csvFileMaker;

    @Before
    public void setup(){
        List<Player> players = handler.getPlayersByTeam("Team 1", "Practice 1");
        List<Integer> teamStats = handler.getTeamStats("Team 1");
        csvFileMaker = new CSVFileMaker(players, "Team 1", teamStats, Collections.singletonList("Practice 1"));
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

    @Test
    public void test_pct_value(){
        List<Integer> stats = Arrays.asList(11, 4, 21, 0, 0, 3, 0, 2, 0, 3, 0, 0);
        String pct = csvFileMaker.calculatePCT(stats);
        Assert.assertEquals(".333", pct);
    }

    @Test
    public void test_pts_value(){
        List<Integer> stats = Arrays.asList(11, 4, 21, 0, 0, 3, 0, 2, 0, 3, 0, 0);
        String pts = csvFileMaker.calculatePTS(stats);
        Assert.assertEquals("12.5", pts);
    }

    @Test
    public void test_formatPCT_remove_zero(){
        String pct = "0.001";
        String formattedPct = csvFileMaker.formatPCT(pct);
        Assert.assertEquals(".001", formattedPct);
    }

    @Test
    public void test_formatPCT_negative(){
        String pct = "-0.275";
        String formattedPct = csvFileMaker.formatPCT(pct);
        Assert.assertEquals("-.275", formattedPct);
    }
}
