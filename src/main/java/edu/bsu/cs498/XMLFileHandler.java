package edu.bsu.cs498;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

    public String printXML() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (TransformerException e) {
            System.out.println("Error");
        }
        return "Error";
    }

    public void addTeam(String teamName, ObservableList<Player>playerList){
        Element root = doc.getDocumentElement();
        Element elementRoot = doc.createElement("Team");
        Node teamNameNode = doc.createElement("TeamName");
        teamNameNode.appendChild(doc.createTextNode(teamName));
        elementRoot.appendChild(teamNameNode);
        for (int i=0; i<playerList.size(); i++){
            addPlayer(elementRoot, teamName, playerList.get(i));
        }
        Node statisticsNode = doc.createElement("Statistics");
        elementRoot.appendChild(statisticsNode);
        root.appendChild(elementRoot);
        System.out.println(printXML());
        updateXML(doc);
    }

    public Node addPlayer(Node teamNode, String teamName, Player player){
        Node playerNode = doc.createElement("Player");
        ((Element) playerNode).setAttribute("id", teamName);
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
        return teamNode;
    }

    public ObservableList<String> getAllTeams(){
        ObservableList<String> teamList = FXCollections.observableArrayList();;
        NodeList teams = doc.getElementsByTagName("TeamName");
        for (int i = 0; i < teams.getLength(); i++) {
            Node currentTeam = teams.item(i);
            String teamName = currentTeam.getTextContent();
            teamList.add(teamName);
            }
        return teamList;
    }

    public ObservableList<String> getTeamPlayers(String teamName){
        ObservableList<String> playerList = FXCollections.observableArrayList();
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList teamNode = null;
        try{XPathExpression expr = xPath.compile("//*[@id='" + teamName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            teamNode = (NodeList) result;
        }
        catch (XPathExpressionException e){ }
        for (int i = 0; i < teamNode.getLength(); i++) {
            Node currentPlayerNode = teamNode.item(i);
            NodeList currentPlayerInfo = currentPlayerNode.getChildNodes();
            String playerName = currentPlayerInfo.item(1).getTextContent();
            String playerNumber = currentPlayerInfo.item(3).getTextContent();
            String playerPosition = currentPlayerInfo.item(5).getTextContent();
            String playerInfo = playerName + "," + playerNumber + "," + playerPosition;
            playerList.add(playerInfo);
        }
        return playerList;
    }

    public void editPlayer(String playerName){

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