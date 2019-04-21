package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeamTest {
    private ObservableList<Player> playerList = FXCollections.observableArrayList();
    private Team testTeam = new Team("Ball State", playerList);

    @Test
    public void getTeamName() {
        assertEquals(testTeam.getTeamName(), "Ball State");
    }
}