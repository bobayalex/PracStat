package edu.bsu.cs498;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class XMLFileHandler {
    private URL url = getClass().getResource("/config/config.xml");
    private File configFile = new File(url.getPath());
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private Document doc;

    XMLFileHandler() {
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

    Document getDoc(){
        return doc;
    }

    private List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        String name;
        int number;
        Node statNode;
        List<String> stats;
        NodeList playerNodes = doc.getElementsByTagName("Player");
        for(int i = 0; i < playerNodes.getLength(); i++){
            Node playerNode = playerNodes.item(i);
            NodeList childNodes = playerNode.getChildNodes();
            name = getChildElement(childNodes, "PlayerName").getTextContent();
            number = Integer.parseInt(getChildElement(childNodes, "PlayerNumber").getTextContent());
            statNode = getChildElement(childNodes, "PlayerStats");
            stats = getPlayerStatList(statNode);
            players.add(new Player(name, number, stats));
        }
        System.out.println(players.size());
        return players;
    }

    private List<Player> getPlayersByTeam(){
        List<Player> players = new ArrayList<>();
        String name;
        int number;
        Node statNode;
        List<String> stats;
        NodeList teamNodes = doc.getElementsByTagName("Team");
        NodeList playerNodes = doc.getElementsByTagName("Player");
        for(int i = 0; i < playerNodes.getLength(); i++){
            Node playerNode = playerNodes.item(i);
            NodeList childNodes = playerNode.getChildNodes();
            name = getChildElement(childNodes, "PlayerName").getTextContent();
            number = Integer.parseInt(getChildElement(childNodes, "PlayerNumber").getTextContent());
            statNode = getChildElement(childNodes, "PlayerStats");
            stats = getPlayerStatList(statNode);
            players.add(new Player(name, number, stats));
        }
        System.out.println(teamNodes.getLength());
        return players;
    }

    private List<String> getPlayerStatList(Node statNode){
        NodeList playerStats = statNode.getChildNodes();
        List<String> stats = new ArrayList<>();
        for(int i = 0; i < playerStats.getLength(); i++){
            if(playerStats.item(i).getNodeType() == Node.ELEMENT_NODE){
                stats.add(playerStats.item(i).getTextContent());            }
        }
        return stats;
    }

    private Node getChildElement(NodeList childNodes, String childElementName) {
        for(int i = 0; i < childNodes.getLength(); i++){
            if(childNodes.item(i).getNodeName().equals(childElementName)){
                return childNodes.item(i);
            }
        }
        return null;
    }

    void test(){
        getPlayersByTeam();
    }

    //////////
    private String printXML() {
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

    public void addTeam(String teamName) {
        Element root = doc.getDocumentElement();
        Element elementRoot = doc.createElement("Team");
        Node teamNameNode = doc.createElement("TeamName");
        teamNameNode.appendChild(doc.createTextNode(teamName));
        elementRoot.appendChild(teamNameNode);
        Node playerNode = doc.createElement("Player");
        Node playerNameNode = doc.createElement("Name");
        Node playerNumberNode = doc.createElement("Number");
        Node playerPositionNode = doc.createElement("Position");
        playerNode.appendChild(playerNameNode);
        playerNode.appendChild(playerNumberNode);
        playerNode.appendChild(playerPositionNode);
        elementRoot.appendChild(playerNode);

        Node statisticsNode = doc.createElement("Statistics");
        elementRoot.appendChild(statisticsNode);
        root.appendChild(elementRoot);
        //System.out.println(printXML());
        updateXML(doc);
    }

    public void updateXML(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            System.out.println("Error");
        }

    }
}