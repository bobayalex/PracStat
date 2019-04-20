package edu.bsu.cs498;

import org.w3c.dom.Document;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (doc == null) {
            return false;
        }
        NodeList teams = doc.getElementsByTagName("Team");
        return teams.getLength() > 0;
    }

    Document getDoc() {
        return doc;
    }

    void setDoc(Document document) {
        doc = document;
    }

    List<Player> getPlayersInAllPractices(String teamName) {
        List<Player> players = new ArrayList<>();
        List<Node> elements = getAllNodes();
        Node practicesNode = getPracticesNode(elements, teamName);
        NodeList childNodes = practicesNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {// this indicates a practice element
                String practiceName = getPracticeName(currentNode);
                List<Player> practicePlayers = getPlayersByTeamPractice(teamName, practiceName);
                players.addAll(practicePlayers);
            }
        }
        return players;
    }

    List<Player> getPlayersInAllPractices2(String teamName) {
        List<Player> players = new ArrayList<>();
        NodeList teamNodes = getNodeList("Team");
        Node teamNode = getParentByChild(teamNodes, teamName);

        System.out.println(teamNode.getNodeName());
        return players;
    }

    private Node getParentByChild(NodeList nodes, String text) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node parent = nodes.item(i);
            for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getTextContent().equals(text)) {
                    return parent;
                }
            }
        }
        return null;
    }

    private Node getChildFromParent(Node parent, String text) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getTextContent().equals(text)) {
                return child;
            }
        }
        return null;
    }

    List<Player> getPlayersByTeamPractice(String teamName, String practiceName) {
        List<Player> players = new ArrayList<>();
        List<Node> elements = getAllNodes();
        Node playersNode = getPlayersNode(elements, teamName, practiceName);
        NodeList playerList = playersNode.getChildNodes(); // this also contains non-element nodes
        for (int i = 0; i < playerList.getLength(); i++) {
            Node currentNode = playerList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) { // elements = players
                addPlayer(currentNode, players, practiceName);
            }
        }
        return players;
    }

    List<String> getPracticesByTeam(String teamName) {
        List<String> practices = new ArrayList<>();
        List<Node> elements = getAllNodes();
        Node practicesNode = getPracticesNode(elements, teamName);
        NodeList childNodes = practicesNode.getChildNodes(); // this also contains non-element nodes
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            if (currentNode.getNodeName().equals("Practice")) {
                String practiceName = getPracticeName(currentNode);
                practices.add(practiceName);
            }
        }
        return practices;
    }

    private String getPracticeName(Node practiceNode) {
        NodeList children = practiceNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeName().equals("PracticeName")) {
                return currentNode.getTextContent();
            }
        }
        return null;
    }

    private void addPlayer(Node playerNode, List<Player> players, String practiceName) {
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
        players.add(new Player(name, number, stats, practiceName));
    }

    private List<Integer> getStatsFromNode(Node statsNode) {
        List<Integer> stats = new ArrayList<>();
        NodeList children = statsNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                String currentText = currentNode.getTextContent();
                if (currentText.equals("")) {
                    stats.add(0);
                } else {
                    stats.add(Integer.parseInt(currentNode.getTextContent()));
                }
            }
        }
        return stats;
    }

//    private void printNodes(NodeList children) {
//        for (int i = 0; i < children.getLength(); i++) {
//            System.out.println(children.item(i).getNodeName());
//        }
//    }

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

    private Node getPracticesNode(List<Node> elements, String teamName) {
        Node currentNode = null;
        for (int i = 0; i < elements.size(); i++) {
            currentNode = elements.get(i);
            if (currentNode.getTextContent().equals(teamName)) {
                currentNode = elements.get(i + 1);
                return currentNode;
            }
        }
        return currentNode;
    }

    List<Node> getAllNodes() {
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

    private NodeList getNodeList(String nodeName) {
        return doc.getElementsByTagName(nodeName);
    }

//    private void printPlayers(List<Player> players) {
//        for (Player player : players) {
//            System.out.println(player.getName());
//            System.out.println(player.getNumber());
//            printStats(player.getStats());
//        }
//    }

//    private void printStats(List<Integer> stats) {
//        System.out.println("Stats:");
//        for (int stat : stats) {
//            System.out.println(stat);
//        }
//    }

    List<Integer> getTeamStats(String teamName) {
        List<Node> elements = getAllNodes();
        Node teamStatsNode = getTeamStatNode(elements, teamName);
        return getStatsFromNode(teamStatsNode);
    }

    private Node getTeamStatNode(List<Node> elements, String teamName) {
        Node currentNode = null;
        for (Node element : elements) {
            currentNode = element;
            if (currentNode.getTextContent().equals(teamName)) {
                currentNode = currentNode.getNextSibling();
                while (!(currentNode.getNodeName().equals("TeamStats"))) {
                    currentNode = currentNode.getNextSibling();
                }
                return currentNode;
            }
        }
        return currentNode;
    }

    void updatePlayerStats(List<Integer> spinnerVals, String teamName, String practiceName) {
        // each player should have 12 stats, process spinnerVals 12 elements at a time
        List<Node> elements = getAllNodes();
        Node playersNode = getPlayersNode(elements, teamName, practiceName);
        NodeList playerList = playersNode.getChildNodes(); // this also contains non-element nodes
        for (int i = 0; i < playerList.getLength(); i++) {
            Node currentPlayerNode = playerList.item(i);
            if (currentPlayerNode.getNodeType() == Node.ELEMENT_NODE) { // elements = players
                NodeList statNodes = getStatNodes(currentPlayerNode);
                updateNodes(Objects.requireNonNull(statNodes), spinnerVals);
            }
        }
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

    private void updateXML(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            System.out.println("Error");
        }

    }


    private void updateNodes(NodeList statNodes, List<Integer> spinnerVals) {
        // statNodes could contain non-elements
        for (int i = 0; i < statNodes.getLength(); i++) {
            Node currentNode = statNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getTextContent().equals("")) {
                    currentNode.setTextContent("0");
                }
                if (!currentNode.getTextContent().equals(String.valueOf(spinnerVals.get(0)))) {
                    currentNode.setTextContent(String.valueOf(spinnerVals.get(0)));
                }
                spinnerVals.remove(0);
            }
        }
        updateXML(doc);
    }

    private NodeList getStatNodes(Node currentPlayerNode) {
        NodeList children = currentPlayerNode.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node currentNode = children.item(j);
            if (currentNode.getNodeName().equals("PlayerStats")) {
                return currentNode.getChildNodes();
            }
        }
        return null;
    }
}