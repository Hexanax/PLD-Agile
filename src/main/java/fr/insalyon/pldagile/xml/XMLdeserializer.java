package fr.insalyon.pldagile.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;


public class XMLdeserializer {


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
        Integer id = Integer.parseInt(elt.getAttribute("id"));
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
        Integer destinationID = Integer.parseInt(elt.getAttribute("destination"));
        Integer originID = Integer.parseInt(elt.getAttribute("origin"));
        if(destinationID < 0){
            throw new ExceptionXML("Error when reading file: Wrong destination ID");
        }
        if(originID < 0) {
            throw new ExceptionXML("Error when reading file: Wrong origin ID");
        }
        String name = elt.getAttribute("name");
        Double length = Double.parseDouble(elt.getAttribute("length"));
        if(length <= 0){
            throw  new ExceptionXML("Error when reading file: Wrong length");
        }
        //TODO : import plan to have the list of intersection
        return new Segment(name,length,null, null);



    }
}
