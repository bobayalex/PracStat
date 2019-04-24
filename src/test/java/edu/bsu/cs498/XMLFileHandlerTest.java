package edu.bsu.cs498;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

public class XMLFileHandlerTest {
    private XMLFileHandler handler;

    @Before
    public void setup(){
        handler = new XMLFileHandler();
    }

    @Test
    public void test_parse_file(){
        Document doc = handler.getDoc();
        Assert.assertNotNull(doc);
    }

    @Test
    public void test_getPlayersInAllPractices(){
        List<Player> players = handler.getPlayersInAllPractices("Team 1");
        Assert.assertEquals(2, players.size());
    }

    @Test
    public void test_getPlayersByTeam(){
        List<Player> players = handler.getPlayersByTeamPractice("Team 1", "Practice 1");
        Assert.assertEquals(2, players.size());
    }

    @Test
    public void test_getNodeList(){
        List<Node> nodes = handler.getAllNodes();
        Assert.assertTrue(nodes.size() > 0);
    }

    @Test
    public void test_getTeamStats(){
        List<Double> teamStats = handler.getTeamStats("Team 1");
        Assert.assertEquals(12, teamStats.size());
    }

    @Test
    public void addPlayer() {
        Assert.assertEquals(4, handler.getNumberOfPlayers("Indiana"));
        PlayerData newPlayerData = new PlayerData("Matt", "46", "Libero");
        handler.addPlayer("Indiana", newPlayerData, handler.getTeamNode("Indiana"));
        Assert.assertEquals(5, handler.getNumberOfPlayers("Indiana"));
    }


    @Test
    public void deletePlayer() {
        Assert.assertEquals(3, handler.getNumberOfPlayers("Michigan"));
        handler.deletePlayer("Kyle");
        Assert.assertEquals(2, handler.getNumberOfPlayers("Michigan"));
        PlayerData kyleReplacement = new PlayerData("Kyle", "22", "Libero");
        handler.addPlayer("Michigan", kyleReplacement, handler.getTeamNode("Michigan"));
    }

    @Test
    public void getAllTeams() {
        Assert.assertEquals(6, handler.getAllTeams().size());
    }

    @Test
    public void getTeamNode() {
        Assert.assertEquals("Team", handler.getTeamNode("Michigan").getNodeName());
    }

    @Test
    public void getAllPlayerNodes() {
        Assert.assertEquals(3, handler.getAllPlayerNodes("Michigan").getLength());
    }

    @Test
    public void getIndividualPlayerNode() {
        Assert.assertEquals("Player", handler.getIndividualPlayerNode("Doug B").getNodeName());
    }

    @Test
    public void getAllPlayersString() {
        Assert.assertEquals(3, handler.getAllPlayersString("Michigan").size());
    }

    @Test
    public void doesTeamExist() {
        Assert.assertTrue(handler.doesTeamExist("Michigan"));
    }

}
