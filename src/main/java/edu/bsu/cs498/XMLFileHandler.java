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

    private Node createTeamNode(String teamName){
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

    public void addTeam(String teamName, ObservableList<Player>playerList){
        Element root = doc.getDocumentElement();
        Node teamRoot = createTeamNode(teamName);
        Node playersNode = teamRoot.getChildNodes().item(1).getFirstChild().getChildNodes().item(1);
        for (int i=0; i<playerList.size(); i++){
            addPlayer(teamName, playerList.get(i), playersNode);
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
        for (int i = 0; i<teamStatsNodes.getLength(); i++){
            teamStatsNodes.item(i).appendChild(doc.createTextNode("0"));
        }
        teamRoot.appendChild(statisticsNode);
        root.appendChild(teamRoot);
        updateXML(doc);
    }

    public void addPlayer(String teamName, Player player, Node playersNode){
        Element playerNode = doc.createElement("Player");
        playerNode.setAttribute("id", teamName);
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
        for (int i = 0; i<playerStatsNodes.getLength(); i++){
            playerStatsNodes.item(i).appendChild(doc.createTextNode("0"));
        }
        playerNode.appendChild(playerNameNode);
        playerNode.appendChild(playerNumberNode);
        playerNode.appendChild(playerPositionNode);
        playerNode.appendChild(playerStatisticsNode);
        playerNameNode.appendChild(doc.createTextNode(player.getPlayerName()));
        playerNumberNode.appendChild(doc.createTextNode(player.getPlayerNumber()));
        playerPositionNode.appendChild(doc.createTextNode(player.getPlayerPosition()));
        playersNode.appendChild(playerNode);
        updateXML(doc);
    }

    public void createPractice(String teamName, String practiceName, ObservableList<Player> participatingPlayers){
        Node practicesNode = getPracticesNode(getTeamNode(teamName));
        Node practiceNode = doc.createElement("Practice");
        Node practiceNameNode = doc.createElement("PracticeName");
        practiceNameNode.appendChild(doc.createTextNode(practiceName));
        Node playersNode = doc.createElement("Players");
        for (int i=0; i<participatingPlayers.size(); i++){
            addPlayer(teamName, participatingPlayers.get(i), playersNode);
        }
        practiceNode.appendChild(practiceNameNode);
        practiceNode.appendChild(playersNode);
        practicesNode.appendChild(practiceNode);
        updateXML(doc);
    }

    public void editPlayer(String oldName, String newName, String newNumber, String newPosition){
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node selectedPlayer = null;
        Node selectedName;
        try{XPathExpression expr = xPath.compile("//*[text()='" + oldName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            selectedName = (Node) result;
            selectedPlayer = selectedName.getParentNode();
        }catch (XPathExpressionException e){}
        NodeList playerInfo = selectedPlayer.getChildNodes();
        for (int i = 0; i < playerInfo.getLength(); i++){
            Node node = playerInfo.item(i);
            if ("PlayerName".equals(node.getNodeName())){node.setTextContent(newName);}
            if ("PlayerNumber".equals(node.getNodeName())){node.setTextContent(newNumber);}
            if ("PlayerPosition".equals(node.getNodeName())){node.setTextContent(newPosition);}
        }
        updateXML(doc);
    }

    public void deletePlayer(String playerName){
        Node selectedPlayer = getIndividualPlayerNode(playerName);
        selectedPlayer.getParentNode().removeChild(selectedPlayer);
        updateXML(doc);
    }

    public int getNumberOfPlayers(String teamName){
        return getAllPlayerNodes(teamName).getLength();
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

    public Node getTeamPlayersNode(Node teamNode){ //Returns "Players" Node given "Team" Parent
        Node teamPlayersNode = null;
        NodeList teamNodeChildren = teamNode.getChildNodes();
        NodeList practiceNodeChildren = teamNodeChildren.item(3).getChildNodes().item(1).getChildNodes();
        for (int i = 0; i<practiceNodeChildren.getLength(); i++){
            if (practiceNodeChildren.item(i).getNodeName().equals("Players")){
                teamPlayersNode = practiceNodeChildren.item(i);
            }
        }
        return teamPlayersNode;
    }

    public Node getPracticesNode(Node teamNode){
        NodeList teamNodeChildren = teamNode.getChildNodes();
        return teamNodeChildren.item(3);
    }

    public Node getIndividualPlayerNode(String playerName){ //Returns parent player node, Name, Number, Position and PlayerStatistics nodes are children
        Node playerNode = null;
        Node playerNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[text()='" + playerName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            playerNameNode = (Node) result;
            playerNode = playerNameNode.getParentNode();
        }catch (XPathExpressionException e){}
        return playerNode;
    }

    public NodeList getAllPlayerNodes(String teamName){ //Returns nodelist of parent player nodes
        NodeList playerNodes = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[@id='" + teamName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            playerNodes = (NodeList) result;
        }catch (XPathExpressionException e){ }
        return playerNodes;
    }

    public ObservableList<String> getAllPlayersString(String teamName){ //Used to populate FXML dropdown boxes
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
                if ("PlayerName".equals(node.getNodeName())){playerName = node.getTextContent();}
                if ("PlayerNumber".equals(node.getNodeName())){playerNumber = node.getTextContent();}
                if ("PlayerPosition".equals(node.getNodeName())){playerPosition = node.getTextContent();}
            }
            String playerInfo = playerName + "," + playerNumber + "," + playerPosition;
            playerList.add(playerInfo);
        }
        return playerList;
    }

    public ObservableList<String> getAllTeams(){ //Used to populate FXML dropdown boxes
        ObservableList<String> teamList = FXCollections.observableArrayList();
        NodeList teams = doc.getElementsByTagName("TeamName");
        for (int i = 0; i < teams.getLength(); i++) {
            Node currentTeam = teams.item(i);
            String teamName = currentTeam.getTextContent();
            teamList.add(teamName);
        }
        return teamList;
    }

    public boolean doesTeamExist(String teamName){
        Node teamNameNode;
        XPath xPath = XPathFactory.newInstance().newXPath();
        try{XPathExpression expr = xPath.compile("//*[text()='" + teamName + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODE);
            teamNameNode = (Node) result;
            if (teamNameNode == null){return false;}
        }catch (XPathExpressionException e){}
        return true;
    }

    private void updateXML(Document doc){
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Result output = new StreamResult(configFile);
            Source input = new DOMSource(doc);
            transformer.transform(input, output);
        }
        catch(TransformerException e){System.out.println("Error");}

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
}