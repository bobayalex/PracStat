package edu.bsu.cs498;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerDataTest {
    private PlayerData testPlayerData = new PlayerData("Mike", "22", "Libero");

    @Test
    public void getPlayerName() {
        assertEquals(testPlayerData.getPlayerName(), "Mike");
    }

    @Test
    public void getPlayerNumber() {
        assertEquals(testPlayerData.getPlayerNumber(), "22");
    }

    @Test
    public void getPlayerPosition() {
        assertEquals(testPlayerData.getPlayerPosition(), "Libero");
    }
}