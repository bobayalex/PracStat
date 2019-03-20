package edu.bsu.cs498;

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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

class XMLFileReader {
    private URL url = getClass().getResource("/config/config.xml");
    private File configFile = new File(url.getPath());
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private Document doc;

    XMLFileReader(){
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
            Player currentPlayer = playerList.get(i);
            Node playerNode = doc.createElement("Player");
            Node playerNameNode = doc.createElement("Name");
            Node playerNumberNode = doc.createElement("Number");
            Node playerPositionNode = doc.createElement("Position");
            playerNode.appendChild(playerNameNode);
            playerNode.appendChild(playerNumberNode);
            playerNode.appendChild(playerPositionNode);
            playerNameNode.appendChild(doc.createTextNode(currentPlayer.getPlayerName()));
            playerNumberNode.appendChild(doc.createTextNode(currentPlayer.getPlayerNumber()));
            playerPositionNode.appendChild(doc.createTextNode(currentPlayer.getPlayerPosition()));
            elementRoot.appendChild(playerNode);
        }
        Node statisticsNode = doc.createElement("Statistics");
        elementRoot.appendChild(statisticsNode);
        root.appendChild(elementRoot);
        System.out.println(printXML());
        updateXML(doc);
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