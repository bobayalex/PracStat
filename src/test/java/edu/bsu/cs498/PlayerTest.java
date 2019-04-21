package edu.bsu.cs498;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player testPlayer = new Player("Mike", "22", "Libero");

    @Test
    public void getPlayerName() {
        assertEquals(testPlayer.getPlayerName(), "Mike");
    }

    @Test
    public void getPlayerNumber() {
        assertEquals(testPlayer.getPlayerNumber(), "22");
    }

    @Test
    public void getPlayerPosition() {
        assertEquals(testPlayer.getPlayerPosition(), "Libero");
    }
}