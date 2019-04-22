package edu.bsu.cs498;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PlayerTest {
    private Player player;
    private String playerName = "Jeff";
    private int playerNumber = 3;
    private List<Double> stats = Arrays.asList(11d, 4d, 21d, 0d, 0d, 3d, 0d, 2d, 0d, 3d, 0d, 0d);
    private String practiceName = "Practice 1";
    @Before
    public void setup() {
        player = new Player(playerName, playerNumber, stats, practiceName);
    }

    @Test
    public void test_getName(){
        Assert.assertTrue(player.getName().equals(playerName));
    }

    @Test
    public void test_getNumber(){
        Assert.assertTrue(player.getNumber() == playerNumber);
    }

    @Test
    public void test_getStats(){
        Assert.assertTrue(player.getStats().equals(stats));
    }

    @Test
    public void test_setStats(){
        List<Double> modifiedStats = Arrays.asList(0d, 1d, 2d, 3d);
        player.setStats(modifiedStats);
        Assert.assertFalse(player.getStats().equals(stats));
    }

    @Test
    public void test_getPractice(){
        Assert.assertTrue(player.getPractice().equals(practiceName));
    }
}
