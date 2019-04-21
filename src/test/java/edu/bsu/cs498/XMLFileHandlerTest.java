package edu.bsu.cs498;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class XMLFileHandlerTest {
    private XMLFileHandler handler = new XMLFileHandler();

    @Test
    public void addPlayer() {
        assertEquals(4, handler.getNumberOfPlayers("Indiana"));
        Player newPlayer = new Player("Matt", "46", "Libero");
        handler.addPlayer("Indiana", newPlayer, handler.getTeamNode("Indiana"));
        assertEquals(5, handler.getNumberOfPlayers("Indiana"));
    }


    @Test
    public void deletePlayer() {
        assertEquals(3, handler.getNumberOfPlayers("Michigan"));
        handler.deletePlayer("Kyle");
        assertEquals(2, handler.getNumberOfPlayers("Michigan"));
        Player kyleReplacement = new Player("Kyle", "22", "Libero");
        handler.addPlayer("Michigan", kyleReplacement, handler.getTeamNode("Michigan"));
    }

    @Test
    public void getAllTeams() {
        assertEquals(6, handler.getAllTeams().size());
    }

    @Test
    public void getTeamNode() {
        assertEquals("Team", handler.getTeamNode("Michigan").getNodeName());
    }

    @Test
    public void getAllPlayerNodes() {
        assertEquals(3, handler.getAllPlayerNodes("Michigan").getLength());
    }

    @Test
    public void getIndividualPlayerNode() {
        assertEquals("Player", handler.getIndividualPlayerNode("Doug B").getNodeName());
    }

    @Test
    public void getAllPlayersString() {
        assertEquals(3, handler.getAllPlayersString("Michigan").size());
    }

    @Test
    public void doesTeamExist() {
        assertTrue(handler.doesTeamExist("Michigan"));
    }
}