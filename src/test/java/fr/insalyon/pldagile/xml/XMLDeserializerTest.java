package fr.insalyon.pldagile.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

class XMLDeserializerTest {

    @Test
    public void loadTest() throws Exception {
//        File xml = XMLFileOpener.getInstance().open(true);
//        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        Document document = docBuilder.parse(xml);
//        Element root = document.getDocumentElement();
//        if (root.getNodeName().equals("map")) {
//            NodeList intersectionsList = root.getElementsByTagName("intersection");
//            Element firstNode = (Element) intersectionsList.item(1);
//        } else {
//            throw new ExceptionXML("Wrong format");
//        }
    }

}