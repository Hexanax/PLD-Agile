package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class XMLdeserializer {

    /**
     * TODO : javadoc
     * @param map
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void load(CityMap map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        Element root = document.getDocumentElement();
        if(root.getNodeName().equals("map")) {
            buildFromDOMXML(root, map);
        }
        else
            throw new ExceptionXML("Wrong format");
    }

    /**
     * TODO : javadoc
     * @param noeudDOMRacine
     * @param map
     * @throws ExceptionXML
     * @throws NumberFormatException
     */
    private static void buildFromDOMXML(Element noeudDOMRacine, CityMap map) throws ExceptionXML, NumberFormatException{
        NodeList intersectionsList = noeudDOMRacine.getElementsByTagName("intersection");
        for(int i=0; i< intersectionsList.getLength(); i++){
            map.add(createIntersection((Element) intersectionsList.item(i)));
        }
    }

    /**
     * TODO : javadoc
     * @param elt
     * @return
     * @throws ExceptionXML
     */
    private static Intersection createIntersection(Element elt) throws  ExceptionXML{
        double latitude = Double.parseDouble(elt.getAttribute("latitude"));
        double longitude = Double.parseDouble(elt.getAttribute("longitude"));
        Coordinates coordinates = new Coordinates(latitude,longitude);
        if(coordinates == null){
            throw new ExceptionXML("Error when reading file: Wrong Coordinates Format");
        }
        Long id = Long.parseLong(elt.getAttribute("id"));
        if(id < 0) {
            throw new ExceptionXML("Error when reading file: id must be null or positive");
        }
        return new Intersection(id, coordinates);
    }

    /**
     * TODO : javadoc
     * @param elt
     * @return
     * @throws ExceptionXML
     */
    private static Segment createSegment(Element elt) throws  ExceptionXML{
        Long destinationID = Long.parseLong(elt.getAttribute("destination"));
        Long originID = Long.parseLong(elt.getAttribute("origin"));
        if(destinationID < 0){
            throw new ExceptionXML("Error when reading file: Wrong destination ID");
        }
        if(originID < 0) {
            throw new ExceptionXML("Error when reading file: Wrong origin ID");
        }
        String name = elt.getAttribute("name");
        double length = Double.parseDouble(elt.getAttribute("length"));
        if(length <= 0){
            throw  new ExceptionXML("Error when reading file: Wrong length");
        }
        //TODO : import plan to have the list of intersection
        return new Segment(name,length,null, null);
    }


}
