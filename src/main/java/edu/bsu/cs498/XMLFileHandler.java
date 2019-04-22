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
import java.util.*;

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
        List<Node> nodes = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        NodeList teamNodes = getNodeList("Team");
        Node teamNode = getParentByChild(teamNodes, teamName);
        nodes = getAllPlayerNodesByTeam(teamNode, nodes);
        for (Node playerNode : nodes) {
            addPlayer(playerNode, players, "");// in this case, practice name doesn't matter
        }
        return players;
    }

    private List<Node> getAllPlayerNodesByTeam(Node teamNode, List<Node> playerNodes) {
        NodeList children = teamNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeName().equals("Player")) {
                playerNodes.add(currentNode);
            }
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                playerNodes = getAllPlayerNodesByTeam(currentNode, playerNodes);
            }
        }
        return playerNodes;
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

//    private Node getChildFromParent(NodeList nodes, String text) {
//        for (int i = 0; i < nodes.getLength(); i++) {
//            Node parent = nodes.item(i);
//            for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
//                if (child.getNodeType() == Node.ELEMENT_NODE && child.getTextContent().equals(text)) {
//                    return child;
//                }
//                if (child.hasChildNodes()) {
//                    return getChildFromParent(child.getChildNodes(), text);
//                }
//            }
//        }
//        return null;
//    }

    List<Player> getPlayersByTeamPractice(String teamName, String practiceName) {
        List<Player> players = new ArrayList<>();
        Node playersNode = getPlayersNode(teamName, practiceName);
        if (!playersNode.getNodeName().equals("Players")) {
            return players;
        }
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
        List<Double> stats = new ArrayList<>();
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

    private List<Double> getStatsFromNode(Node statsNode) {
        List<Double> stats = new ArrayList<>();
        NodeList children = statsNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node currentNode = children.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                String currentText = currentNode.getTextContent();
                if (currentText.equals("")) {// in the event that there is not a number, make it 0
                    stats.add(0d);
                } else {
                    stats.add(Double.parseDouble(currentNode.getTextContent()));
                }
            }
        }
        return stats;
    }

    private Node getPlayersNode(String teamName, String practiceName) {
        Node playersNode;
        Map<Node, Node> nodeMap = new HashMap<>();
        NodeList elements = doc.getDocumentElement().getChildNodes();
        Node teamNode = getTeamNode(elements, teamName, nodeMap);
        nodeMap.clear();
        elements = teamNode.getChildNodes();
        playersNode = getPlayersNodeRec(elements, practiceName, nodeMap);
        return playersNode;
    }

    private Node getTeamNode(NodeList elements, String teamName, Map<Node, Node> nodeMap) {
        for (int i = 0; i < elements.getLength(); i++) {
            Node currentNode = elements.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getTextContent().equals(teamName)) {
                nodeMap.put(currentNode.getParentNode(), currentNode.getParentNode());
            } else if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getTeamNode(currentNode.getChildNodes(), teamName, nodeMap);
            }
        }
        if (nodeMap.isEmpty()) {
            return null;
        }
        return nodeMap.get(nodeMap.values().toArray()[0]);
    }

    private Node getPlayersNodeRec(NodeList elements, String practiceName, Map<Node, Node> nodeMap) {
        for (int i = 0; i < elements.getLength(); i++) {
            Node currentNode = elements.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getTextContent().equals(practiceName)) {
                currentNode = currentNode.getNextSibling();
                while (currentNode.getNodeType() != Node.ELEMENT_NODE) {
                    currentNode = currentNode.getNextSibling();
                }
                nodeMap.put(currentNode, currentNode);
            } else if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getPlayersNodeRec(currentNode.getChildNodes(), practiceName, nodeMap);
            }
        }
        if (nodeMap.isEmpty()) {
            return null;
        }
        return nodeMap.get(nodeMap.values().toArray()[0]);
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
        return getAllNodesRec(node, nodes);
    }

    private List<Node> getAllNodesRec(Node node, List<Node> nodes) {
        nodes.add(node);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getAllNodesRec(currentNode, nodes);
            }
        }
        return nodes;
    }

    private NodeList getNodeList(String nodeName) {
        return doc.getElementsByTagName(nodeName);
    }

    List<Double> getTeamStats(String teamName) {
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

    boolean updatePlayerStats(List<Integer> spinnerVals, String teamName, String practiceName) {
        try {
            // each player should have 12 stats, process spinnerVals 12 elements at a time
            Node playersNode = getPlayersNode(teamName, practiceName);
            NodeList playerList = playersNode.getChildNodes(); // this also contains non-element nodes
            for (int i = 0; i < playerList.getLength(); i++) {
                Node currentPlayerNode = playerList.item(i);
                if (currentPlayerNode.getNodeType() == Node.ELEMENT_NODE) { // elements = players
                    NodeList statNodes = getStatNodes(currentPlayerNode);
                    updateNodes(Objects.requireNonNull(statNodes), spinnerVals);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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