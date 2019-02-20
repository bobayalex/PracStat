package edu.bsu.cs498;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
}