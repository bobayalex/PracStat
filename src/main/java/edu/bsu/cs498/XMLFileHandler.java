package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;


class XMLFileHandler {
    private URL url = getClass().getResource("/config/config.xml");
    private File configFile = new File(url.getPath());
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private Document doc;
    XMLFileHandler(){
        parseFile();
    }

    private void parseFile() {
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(configFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    boolean isConfigured() {
        NodeList teams = doc.getElementsByTagName("Team");
        return teams.getLength() > 0;
    }

    private String printXML() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (TransformerException e) {
            System.out.println("Error");
        }
        return "Error";
    }

    private Node createTeamNode(String teamName){
        Element teamRoot = doc.createElement("Team");
        Node teamNameNode = doc.createElement("TeamName");
        teamNameNode.appendChild(doc.createTextNode(teamName));
        teamRoot.appendChild(teamNameNode);
        return teamRoot;
    }

    public void addTeam(String teamName, ObservableList<Player>playerList){
        Element root = doc.getDocumentElement();
        Node teamRoot = createTeamNode(teamName);
        for (int i=0; i<playerList.size(); i++){
            addPlayer(teamName, playerList.get(i), teamRoot);
        }
        Node statisticsNode = doc.createElement("Statistics");
        teamRoot.appendChild(statisticsNode);
        root.appendChild(teamRoot);
        System.out.println(printXML());
        updateXML(doc);
    }

    public void addPlayer(String teamName, Player player, Node teamNode){
        Element playerNode = doc.createElement("Player");
        playerNode.setAttribute("id", teamName);
        Node playerNameNode = doc.createElement("Name");
        Node playerNumberNode = doc.createElement("Number");
        Node playerPositionNode = doc.createElement("Position");
        Node playerStatisticsNode = doc.createElement("PlayerStatistics");
        playerNode.appendChild(playerNameNode);
        playerNode.appendChild(playerNumberNode);
        playerNode.appendChild(playerPositionNode);
        playerNode.appendChild(playerStatisticsNode);
        playerNameNode.appendChild(doc.createTextNode(player.getPlayerName()));
        playerNumberNode.appendChild(doc.createTextNode(player.getPlayerNumber()));
        playerPositionNode.appendChild(doc.createTextNode(player.getPlayerPosition()));
        teamNode.appendChild(playerNode);
        updateXML(doc);
    }

    public void editPlayer(String oldName, String newName, String newNumber, String newPosition){
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node selectedPlayer = null;
        Node selectedName;
        try{XPathExpression expr = xPath.compile("//*[text()[contains(.,'" + oldName + "')]]");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            selectedName = (Node) result;
            selectedPlayer = selectedName.getParentNode();
        }catch (XPathExpressionException e){}
        NodeList playerInfo = selectedPlayer.getChildNodes();
        for (int i = 0; i < playerInfo.getLength(); i++){
            Node node = playerInfo.item(i);
            if ("Name".equals(node.getNodeName())){node.setTextContent(newName);}
            if ("Number".equals(node.getNodeName())){node.setTextContent(newNumber);}
            if ("Position".equals(node.getNodeName())){node.setTextContent(newPosition);}
        }
        System.out.println(playerInfo.getLength());
        updateXML(doc);
    }

    public void deletePlayer(String playerName){
        Node selectedPlayer = getIndividualPlayerNode(playerName);
        selectedPlayer.getParentNode().removeChild(selectedPlayer);
        updateXML(doc);
    }

    public ObservableList<String> getAllTeams(){
        ObservableList<String> teamList = FXCollections.observableArrayList();
        NodeList teams = doc.getElementsByTagName("TeamName");
        for (int i = 0; i < teams.getLength(); i++) {
            Node currentTeam = teams.item(i);
            String teamName = currentTeam.getTextContent();
            teamList.add(teamName);
            }
        return teamList;
    }

    public Node getTeamNode(String teamName){
        Node teamNode = null;
        Node teamNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[text()[contains(.,'" + teamName + "')]]");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            teamNameNode = (Node) result;
            if (teamNameNode == null){teamNode = createTeamNode(teamName);}
            else {teamNode = teamNameNode.getParentNode();}
        }catch (XPathExpressionException e){}
        return teamNode;
    }

    public NodeList getAllPlayerNodes(String teamName){
        NodeList playerNodes = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[@id='" + teamName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            playerNodes = (NodeList) result;
        }catch (XPathExpressionException e){ }
        return playerNodes;
    }

    public Node getIndividualPlayerNode(String playerName){
        Node playerNode = null;
        Node playerNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[text()[contains(.,'" + playerName + "')]]");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            playerNameNode = (Node) result;
            playerNode = playerNameNode.getParentNode();
        }catch (XPathExpressionException e){}
        return playerNode;
    }

    public ObservableList<String> getAllPlayersString(String teamName){
        String playerName = "";
        String playerNumber = "";
        String playerPosition = "";
        ObservableList<String> playerList = FXCollections.observableArrayList();
        NodeList playerNodes = getAllPlayerNodes(teamName);
        for (int i = 0; i < playerNodes.getLength(); i++) {
            Node currentPlayerNode = playerNodes.item(i);
            NodeList currentPlayerInfo = currentPlayerNode.getChildNodes();
            for (int j = 0; j < currentPlayerInfo.getLength(); j++){
                Node node = currentPlayerInfo.item(j);
                if ("Name".equals(node.getNodeName())){playerName = node.getTextContent();}
                if ("Number".equals(node.getNodeName())){playerNumber = node.getTextContent();}
                if ("Position".equals(node.getNodeName())){playerPosition = node.getTextContent();}
            }
            String playerInfo = playerName + "," + playerNumber + "," + playerPosition;
            playerList.add(playerInfo);
        }
        return playerList;
    }

    public boolean doesTeamExist(String teamName){
        Node teamNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[text()[contains(.,'" + teamName + "')]]");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            teamNameNode = (Node) result;
            if (teamNameNode == null){return false;}
        }catch (XPathExpressionException e){}
        return true;
    }

    public void updateXML(Document doc){
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
            System.out.println("Updated");
        }
        catch(TransformerException e){System.out.println("Error");}

    }
}