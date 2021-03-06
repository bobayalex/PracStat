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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

class XMLFileHandler {
    private File jarFile = new File(XMLFileHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    private File configFile = new File(jarFile.getParentFile(), "/config.xml");
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

    Document getDoc() {
        return doc;
    }

    List<Player> getPlayersInAllPractices(String teamName) {
        List<Node> nodes = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        NodeList teamNodes = getTeamNodeList();
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
        if (teamNode != null) {
            elements = teamNode.getChildNodes();
        }
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

    private NodeList getTeamNodeList() {
        return doc.getElementsByTagName("Team");
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
            List<Integer> spinnerValsCopy = new ArrayList<>(spinnerVals);
            Node playersNode = getPlayersNode(teamName, practiceName);
            NodeList playerList = playersNode.getChildNodes(); // this also contains non-element nodes
            Node seasonStats = getSeasonStatsNode(teamName);// seasonStats is a practice Node
            List<Node> seasonStatNodes = new ArrayList<>();
            for (int i = 0; i < playerList.getLength(); i++) {
                Node currentPlayerNode = playerList.item(i);
                if (currentPlayerNode.getNodeType() == Node.ELEMENT_NODE) { // elements = players
                    NodeList statNodes = getStatNodes(currentPlayerNode);
                    updateNodes(Objects.requireNonNull(statNodes), spinnerVals);
                }
            }
            int numPlayersInPractice = getPlayersByTeamPractice(teamName, practiceName).size();
            int totalNumPlayers = getNumberOfPlayers(teamName);
            if (numPlayersInPractice == totalNumPlayers) {
                getSeasonStatsFromNode(seasonStats, seasonStatNodes, totalNumPlayers);
                for (int j = 0; j < totalNumPlayers; j++) {
                    NodeList statNodes = seasonStatNodes.get(j).getChildNodes();// seasonStatNodes should always have more elements than the number of players
                    updateSeasonNodes(Objects.requireNonNull(statNodes), spinnerValsCopy);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void updateSeasonNodes(NodeList statNodes, List<Integer> spinnerVals) {
        // statNodes likely contains non-element nodes
        for (int i = 0; i < statNodes.getLength(); i++) {
            Node currentNode = statNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getTextContent().equals("")) {
                    currentNode.setTextContent("0");
                }
                String currentValue = currentNode.getTextContent();
                if (!currentValue.equals(String.valueOf(spinnerVals.get(0)))) {
                    int valueToAdd = spinnerVals.get(0);
                    currentNode.setTextContent(String.valueOf(Integer.parseInt(currentValue) + valueToAdd));
                }
                spinnerVals.remove(0);
            }
        }
        updateXML(doc);
    }

    private void getSeasonStatsFromNode(Node node, List<Node> foundNode, int totalNumPlayers) {
        if (foundNode.size() == totalNumPlayers ) {
            return;
        }
        NodeList children = node.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node currentNode = children.item(j);
            if (currentNode.getNodeName().equals("PlayerStats")) {
                foundNode.add(currentNode);
            }
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                getSeasonStatsFromNode(currentNode, foundNode, totalNumPlayers);
            }
        }
    }

    private Node getSeasonStatsNode(String teamName) {
        List<Node> elements = getAllNodes();
        Node practices = getPracticesNode(elements, teamName);
        NodeList children = practices.getChildNodes();
        Node currentNode = null;
        for (int i = 0; i < children.getLength(); i++) {
            currentNode = children.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equals("Practice")) {
                return currentNode;
            }
        }
        return currentNode;
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

    private Node createTeamNode(String teamName) {
        Element teamRoot = doc.createElement("Team");
        Node teamNameNode = doc.createElement("TeamName");
        Node practicesNode = doc.createElement("Practices");
        Node practiceNode = doc.createElement("Practice");
        Node practiceNameNode = doc.createElement("PracticeName");
        practiceNameNode.appendChild(doc.createTextNode("SeasonStats"));
        Node playersNode = doc.createElement("Players");
        teamNameNode.appendChild(doc.createTextNode(teamName));
        teamRoot.appendChild(teamNameNode);
        practiceNode.appendChild(practiceNameNode);
        practiceNode.appendChild(playersNode);
        practicesNode.appendChild(practiceNode);
        teamRoot.appendChild(practicesNode);
        return teamRoot;
    }

    void addTeam(String teamName, ObservableList<PlayerData> playerDataList) {
        Element root = doc.getDocumentElement();
        Node teamRoot = createTeamNode(teamName);
        Node playersNode = teamRoot.getChildNodes().item(1).getFirstChild().getChildNodes().item(1);
        for (PlayerData playerData : playerDataList) {
            addPlayer(teamName, playerData, playersNode, true);
        }
        Node statisticsNode = doc.createElement("TeamStats");
        Node killsNode = doc.createElement("Kills");
        Node errorsNode = doc.createElement("Errors");
        Node totalAttemptsNode = doc.createElement("TotalAttempts");
        Node assistsNode = doc.createElement("Assists");
        Node serviceAcesNode = doc.createElement("ServiceAces");
        Node serviceErrorsNode = doc.createElement("ServiceErrors");
        Node receptionErrorsNode = doc.createElement("ReceptionErrors");
        Node digsNode = doc.createElement("Digs");
        Node soloBlocksNode = doc.createElement("SoloBlocks");
        Node blockAssistsNode = doc.createElement("BlockAssists");
        Node blockingErrorsNode = doc.createElement("BlockingErrors");
        Node ballHandlingErrors = doc.createElement("BallHandlingErrors");
        statisticsNode.appendChild(killsNode);
        statisticsNode.appendChild(errorsNode);
        statisticsNode.appendChild(totalAttemptsNode);
        statisticsNode.appendChild(assistsNode);
        statisticsNode.appendChild(serviceAcesNode);
        statisticsNode.appendChild(serviceErrorsNode);
        statisticsNode.appendChild(receptionErrorsNode);
        statisticsNode.appendChild(digsNode);
        statisticsNode.appendChild(soloBlocksNode);
        statisticsNode.appendChild(blockAssistsNode);
        statisticsNode.appendChild(blockingErrorsNode);
        statisticsNode.appendChild(ballHandlingErrors);
        NodeList teamStatsNodes = statisticsNode.getChildNodes();
        for (int i = 0; i < teamStatsNodes.getLength(); i++) {
            teamStatsNodes.item(i).appendChild(doc.createTextNode("0"));
        }
        teamRoot.appendChild(statisticsNode);
        root.appendChild(teamRoot);
        updateXML(doc);
    }

    void addPlayer(String teamName, PlayerData playerData, Node playersNode, boolean isSeasonStats) {
        Element playerNode = doc.createElement("Player");
        if (isSeasonStats) {
            playerNode.setAttribute("id", teamName + "Season");
        } else {
            playerNode.setAttribute("id", teamName);
        }
        Node playerNameNode = doc.createElement("PlayerName");
        Node playerNumberNode = doc.createElement("PlayerNumber");
        Node playerPositionNode = doc.createElement("PlayerPosition");
        Node playerStatisticsNode = doc.createElement("PlayerStats");
        Node killsNode = doc.createElement("Kills");
        Node errorsNode = doc.createElement("Errors");
        Node totalAttemptsNode = doc.createElement("TotalAttempts");
        Node assistsNode = doc.createElement("Assists");
        Node serviceAcesNode = doc.createElement("ServiceAces");
        Node serviceErrorsNode = doc.createElement("ServiceErrors");
        Node receptionErrorsNode = doc.createElement("ReceptionErrors");
        Node digsNode = doc.createElement("Digs");
        Node soloBlocksNode = doc.createElement("SoloBlocks");
        Node blockAssistsNode = doc.createElement("BlockAssists");
        Node blockingErrorsNode = doc.createElement("BlockingErrors");
        Node ballHandlingErrors = doc.createElement("BallHandlingErrors");
        playerStatisticsNode.appendChild(killsNode);
        playerStatisticsNode.appendChild(errorsNode);
        playerStatisticsNode.appendChild(totalAttemptsNode);
        playerStatisticsNode.appendChild(assistsNode);
        playerStatisticsNode.appendChild(serviceAcesNode);
        playerStatisticsNode.appendChild(serviceErrorsNode);
        playerStatisticsNode.appendChild(receptionErrorsNode);
        playerStatisticsNode.appendChild(digsNode);
        playerStatisticsNode.appendChild(soloBlocksNode);
        playerStatisticsNode.appendChild(blockAssistsNode);
        playerStatisticsNode.appendChild(blockingErrorsNode);
        playerStatisticsNode.appendChild(ballHandlingErrors);
        NodeList playerStatsNodes = playerStatisticsNode.getChildNodes();
        for (int i = 0; i < playerStatsNodes.getLength(); i++) {
            playerStatsNodes.item(i).appendChild(doc.createTextNode("0"));
        }
        playerNode.appendChild(playerNameNode);
        playerNode.appendChild(playerNumberNode);
        playerNode.appendChild(playerPositionNode);
        playerNode.appendChild(playerStatisticsNode);
        playerNameNode.appendChild(doc.createTextNode(playerData.getPlayerName()));
        playerNumberNode.appendChild(doc.createTextNode(playerData.getPlayerNumber()));
        playerPositionNode.appendChild(doc.createTextNode(playerData.getPlayerPosition()));
        playersNode.appendChild(playerNode);
        updateXML(doc);
    }

    void createPractice(String teamName, String
            practiceName, ObservableList<PlayerData> participatingPlayersData) {
        Node practicesNode = getPracticesNode(getTeamNode(teamName));
        Node practiceNode = doc.createElement("Practice");
        Node practiceNameNode = doc.createElement("PracticeName");
        practiceNameNode.appendChild(doc.createTextNode(practiceName));
        Node playersNode = doc.createElement("Players");
        for (PlayerData playerData : participatingPlayersData) {
            addPlayer(teamName, playerData, playersNode, false);
        }
        practiceNode.appendChild(practiceNameNode);
        practiceNode.appendChild(playersNode);
        practicesNode.appendChild(practiceNode);
        updateXML(doc);
    }

    void editPlayer(String oldName, String newName, String newNumber, String newPosition) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node selectedPlayer = null;
        Node selectedName;
        try {
            XPathExpression expr = xPath.compile("//*[text()='" + oldName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            selectedName = (Node) result;
            selectedPlayer = selectedName.getParentNode();
        } catch (XPathExpressionException ignored) {
        }
        NodeList playerInfo = null;
        if (selectedPlayer != null) {
            playerInfo = selectedPlayer.getChildNodes();
        }
        if (playerInfo != null) {
            for (int i = 0; i < playerInfo.getLength(); i++) {
                Node node = playerInfo.item(i);
                if ("PlayerName".equals(node.getNodeName())) {
                    node.setTextContent(newName);
                }
                if ("PlayerNumber".equals(node.getNodeName())) {
                    node.setTextContent(newNumber);
                }
                if ("PlayerPosition".equals(node.getNodeName())) {
                    node.setTextContent(newPosition);
                }
            }
        }
        updateXML(doc);
    }

    void deletePlayer(String playerName) {
        Node selectedPlayer = getIndividualPlayerNode(playerName);
        selectedPlayer.getParentNode().removeChild(selectedPlayer);
        updateXML(doc);
    }

    int getNumberOfPlayers(String teamName) {
        return getAllPlayerNodes(teamName).getLength();
    }

    Node getTeamNode(String teamName) {
        Node teamNode = null;
        Node teamNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expr = xPath.compile("//*[text()[contains(.,'" + teamName + "')]]");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            teamNameNode = (Node) result;
            if (teamNameNode == null) {
                teamNode = createTeamNode(teamName);
            } else {
                teamNode = teamNameNode.getParentNode();
            }
        } catch (XPathExpressionException ignored) {
        }
        return teamNode;
    }

    Node getTeamPlayersNode(Node teamNode) { //Returns "Players" Node given "Team" Parent
        Node teamPlayersNode = null;
        NodeList teamNodeChildren = teamNode.getChildNodes();
        NodeList practiceNodeChildren = teamNodeChildren.item(3).getChildNodes().item(1).getChildNodes();
        for (int i = 0; i < practiceNodeChildren.getLength(); i++) {
            if (practiceNodeChildren.item(i).getNodeName().equals("Players")) {
                teamPlayersNode = practiceNodeChildren.item(i);
            }
        }
        return teamPlayersNode;
    }

    private Node getPracticesNode(Node teamNode) {
        NodeList teamNodeChildren = teamNode.getChildNodes();
        return teamNodeChildren.item(3);
    }

    Node getIndividualPlayerNode(String playerName) { //Returns parent player node, Name, Number, Position and PlayerStatistics nodes are children
        Node playerNode = null;
        Node playerNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expr = xPath.compile("//*[text()='" + playerName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            playerNameNode = (Node) result;
            playerNode = playerNameNode.getParentNode();
        } catch (XPathExpressionException ignored) {
        }
        return playerNode;
    }

    NodeList getAllPlayerNodes(String teamName) { //Returns nodelist of parent player nodes
        NodeList playerNodes = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expr = xPath.compile("//*[@id='" + teamName + "Season" + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            playerNodes = (NodeList) result;
        } catch (XPathExpressionException ignored) {
        }
        return playerNodes;
    }

    ObservableList<String> getAllPlayersString(String teamName) { //Used to populate FXML dropdown boxes and lists
        String playerName = "";
        String playerNumber = "";
        String playerPosition = "";
        ObservableList<String> playerList = FXCollections.observableArrayList();
        NodeList playerNodes = getAllPlayerNodes(teamName);
        for (int i = 0; i < playerNodes.getLength(); i++) {
            Node currentPlayerNode = playerNodes.item(i);
            NodeList currentPlayerInfo = currentPlayerNode.getChildNodes();
            boolean duplicate = false;
            for (int j = 0; j < currentPlayerInfo.getLength(); j++) {
                Node node = currentPlayerInfo.item(j);
                if ("PlayerName".equals(node.getNodeName())) {
                    playerName = node.getTextContent();
                }
                if ("PlayerNumber".equals(node.getNodeName())) {
                    playerNumber = node.getTextContent();
                }
                if ("PlayerPosition".equals(node.getNodeName())) {
                    playerPosition = node.getTextContent();
                }
            }
            String playerInfo = playerName + "," + playerNumber + "," + playerPosition;
            for (String str : playerList) {
                if (str.contains(playerInfo)) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                playerList.add(playerInfo);
            }
        }
        return playerList;
    }

    ObservableList<String> getPracticesByTeamObservable(String teamName) {
        return FXCollections.observableArrayList(getPracticesByTeam(teamName));
    }

    ObservableList<String> getAllTeams() { //Used to populate FXML dropdown boxes
        ObservableList<String> teamList = FXCollections.observableArrayList();
        NodeList teams = doc.getElementsByTagName("TeamName");
        for (int i = 0; i < teams.getLength(); i++) {
            Node currentTeam = teams.item(i);
            String teamName = currentTeam.getTextContent();
            teamList.add(teamName);
        }
        return teamList;
    }

    boolean doesTeamExist(String teamName) {
        Node teamNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expr = xPath.compile("//*[text()='" + teamName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            teamNameNode = (Node) result;
            if (teamNameNode == null) {
                return false;
            }
        } catch (XPathExpressionException ignored) {
        }
        return true;
    }

    private void updateXML(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            System.out.println("Error");
        }
    }
}