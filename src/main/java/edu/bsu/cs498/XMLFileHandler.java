package edu.bsu.cs498;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

    Document getDoc() {
        return doc;
    }

    List<Player> getPlayersByTeam(String teamName, String practiceName) {
        List<Player> players = new ArrayList<>();
        List<Node> elements = getNodeList();
        Node playersNode = getPlayersNode(elements, teamName, practiceName);
        NodeList playerList = playersNode.getChildNodes(); // this also contains non-element nodes

        for (int i = 0; i < playerList.getLength(); i++) {
            Node currentNode = playerList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) { // elements = players
                addPlayer(currentNode, players);
            }
        }
        return players;
    }

    private void addPlayer(Node playerNode, List<Player> players) {
        NodeList children = playerNode.getChildNodes();
        String name = "";
        int number = -1;
        List<Integer> stats = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeName().equals("PlayerName")) {
                name = currentNode.getTextContent();
            }
            if (currentNode.getNodeName().equals("PlayerNumber")) {
                number = Integer.parseInt(currentNode.getTextContent());
            }
            if (currentNode.getNodeName().equals("PlayerStats")) {
                stats = getStatsFromNode(currentNode);
            }
        }
        players.add(new Player(name, number, stats));
    }

    private List<Integer> getStatsFromNode(Node statsNode) {
        List<Integer> stats = new ArrayList<>();
        NodeList children = statsNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                stats.add(Integer.parseInt(currentNode.getTextContent()));
            }
        }
        return stats;
    }

    private void printNodes(NodeList children) {
        for (int i = 0; i < children.getLength(); i++) {
            System.out.println(children.item(i).getNodeName());
        }
    }

    private Node getPlayersNode(List<Node> elements, String teamName, String practiceName) {
        Node currentNode = null;
        for (int i = 0; i < elements.size(); i++) {
            currentNode = elements.get(i);
            if (currentNode.getTextContent().equals(teamName)) {
                currentNode = elements.get(i + 3);
                if (currentNode.getTextContent().equals(practiceName)) {
                    currentNode = elements.get(i + 4); // players element
                    return currentNode;
                }
            }
        }
        return currentNode;
    }

    private List<Node> getNodeList() {
        Node node = doc.getDocumentElement();
        List<Node> nodes = new ArrayList<>();
        return getAllNodes(node, nodes);
    }

    private List<Node> getAllNodes(Node node, List<Node> nodes) {
        nodes.add(node);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getAllNodes(currentNode, nodes);
            }
        }
        return nodes;
    }

    void test() {
        getTeamStats("Team 1");
    }

    private void printPlayers(List<Player> players) {
        for (Player player : players) {
            System.out.println(player.getName());
            System.out.println(player.getNumber());
            printStats(player.getStats());
        }
    }

    private void printStats(List<Integer> stats) {
        System.out.println("Stats:");
        for (int stat : stats) {
            System.out.println(stat);
        }
    }

    public List<Integer> getTeamStats(String teamName) {
        List<Node> elements = getNodeList();
        Node teamStatsNode = getTeamStatNode(elements, teamName);
        List<Integer> teamStats = getStatsFromNode(teamStatsNode);
        return teamStats;
    }

    private Node getTeamStatNode(List<Node> elements, String teamName) {
        Node currentNode = null;
        for (int i = 0; i < elements.size(); i++) {
            currentNode = elements.get(i);
            if (currentNode.getTextContent().equals(teamName)) {
                currentNode = currentNode.getNextSibling();
                while (!(currentNode.getNodeName().equals("TeamStats"))){
                    currentNode = currentNode.getNextSibling();
                }
                return currentNode;
            }
        }
        return currentNode;
    }

    //////////
//    private String printXML() {
//        try {
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//            StreamResult result = new StreamResult(new StringWriter());
//            DOMSource source = new DOMSource(doc);
//            transformer.transform(source, result);
//            String xmlString = result.getWriter().toString();
//            return xmlString;
//        } catch (TransformerException e) {
//            System.out.println("Error");
//        }
//        return "Error";
//    }
//
//    public void addTeam(String teamName) {
//        Element root = doc.getDocumentElement();
//        Element elementRoot = doc.createElement("Team");
//        Node teamNameNode = doc.createElement("TeamName");
//        teamNameNode.appendChild(doc.createTextNode(teamName));
//        elementRoot.appendChild(teamNameNode);
//        Node playerNode = doc.createElement("Player");
//        Node playerNameNode = doc.createElement("Name");
//        Node playerNumberNode = doc.createElement("Number");
//        Node playerPositionNode = doc.createElement("Position");
//        playerNode.appendChild(playerNameNode);
//        playerNode.appendChild(playerNumberNode);
//        playerNode.appendChild(playerPositionNode);
//        elementRoot.appendChild(playerNode);
//
//        Node statisticsNode = doc.createElement("Statistics");
//        elementRoot.appendChild(statisticsNode);
//        root.appendChild(elementRoot);
//        //System.out.println(printXML());
//        updateXML(doc);
//    }
//
//    public void updateXML(Document doc) {
//        try {
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            Result output = new StreamResult(configFile);
//            Source input = new DOMSource(doc);
//            transformer.transform(input, output);
//        } catch (TransformerException e) {
//            System.out.println("Error");
//        }
//
//    }
}