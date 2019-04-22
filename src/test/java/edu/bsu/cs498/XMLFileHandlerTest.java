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
    public void test_configured(){
        boolean isConfigured = handler.isConfigured();
        Assert.assertTrue(isConfigured);
    }

    @Test
    public void test_notConfigured(){
        handler.setDoc(null);
        boolean isConfigured = handler.isConfigured();
        Assert.assertFalse(isConfigured);
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

}
