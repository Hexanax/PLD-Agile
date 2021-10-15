package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.*;


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
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;


public class XMLdeserializer {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("k:m:s");

    /**
     * TODO : javadoc
     * @param map
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void load(CityMap map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        Element root = getRootElement();
        if(root.getNodeName().equals("map")) {
            buildFromDOMXML(root, map);
        }
        else
            throw new ExceptionXML("Wrong format");
    }

    private static void buildFromDOMXML(Element noeudDOMRacine, CityMap map) throws ExceptionXML {
        NodeList intersectionsList = noeudDOMRacine.getElementsByTagName("intersection");
        for(int i=0; i< intersectionsList.getLength(); i++){
            map.add(createIntersection((Element) intersectionsList.item(i)));
        }
        NodeList segmentsList = noeudDOMRacine.getElementsByTagName("segment");
        for(int i=0; i< intersectionsList.getLength(); i++){
            map.add(createSegment((Element) segmentsList.item(i), map.getIntersections()));
        }
    }

    private static Segment createSegment(Element elt, Map<Long, Intersection> intersections) throws ExceptionXML {
        Long destinationID = Long.parseLong(elt.getAttribute("destination"));
        Long originID = Long.parseLong(elt.getAttribute("origin"));
        if(destinationID < 0){
            throw new ExceptionXML("Error when reading file: Wrong destination ID");
        }
        Intersection destination = intersections.get(destinationID);
        if(destination == null){
            throw new ExceptionXML("Error when reading file: An intersection is unknown");
        }
        if(originID < 0) {
            throw new ExceptionXML("Error when reading file: Wrong origin ID");
        }
        Intersection origin = intersections.get(originID);
        if(origin == null){
            throw new ExceptionXML("Error when reading file: An intersection is unknown");
        }

        String name = elt.getAttribute("name");
        double length = Double.parseDouble(elt.getAttribute("length"));
        if(length <= 0){
            throw  new ExceptionXML("Error when reading file: Wrong length");
        }
        return new Segment(name,length,origin, destination);
    }

    private static Intersection createIntersection(Element elt) throws ExceptionXML {
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


    public static void load(PlanningRequest planning, CityMap map) throws ParserConfigurationException, SAXException, IOException, ExceptionXML, ParseException {
        Element root = getRootElement();
        if(root.getNodeName().equals("planningRequest")) {
            buildFromDOMXML(root, planning, map);
        }
        else
            throw new ExceptionXML("Wrong format");
    }

    private static void buildFromDOMXML(Element noeudDOMRacine, PlanningRequest planning, CityMap map) throws ExceptionXML, ParseException {
        NodeList depot = noeudDOMRacine.getElementsByTagName("depot");
        if(depot.getLength() != 1)
        {
            throw new ExceptionXML("Error when reading file : Depot is undefined");
        }

        Map<Long, Intersection> intersections = map.getIntersections();
        planning.add(createDepot((Element) depot.item(0), intersections));

        NodeList requests = noeudDOMRacine.getElementsByTagName("request");
        for(int i = 0; i < requests.getLength(); i++)
        {
            planning.add(createRequest((Element) requests.item(i),intersections));
        }
    }

    private static Request createRequest(Element request, Map<Long, Intersection> intersections) throws ExceptionXML{
        Long pickupAddress = Long.parseLong(request.getAttribute("pickupAddress"));
        Long deliveryAddress = Long.parseLong(request.getAttribute("deliveryAddress"));
        if(pickupAddress.equals(deliveryAddress)){
            throw new ExceptionXML("Error when reading file: pickup address can't be at the same place as delivery address");
        }
        Intersection pickup = intersections.get(pickupAddress);
        Intersection delivery = intersections.get(deliveryAddress);
        //TODO : ask to client if a planning request can have 2 request with the same pickup and the same delivery
        if(pickup ==null){
            throw new ExceptionXML("Error when reading file: The pickup address doesn't match with the current city map");
        }
        if(delivery == null){
            throw new ExceptionXML("Error when reading file: The delivery address doesn't match with the current city map");
        }

        int pickupDuration = Integer.parseInt(request.getAttribute("pickupDuration"));
        int deliveryDuration = Integer.parseInt(request.getAttribute("deliveryDuration"));
        if(pickupDuration < 0) {
            throw new ExceptionXML("Error when reading file: The pickup duration can't be negative");
        }
        if(deliveryDuration < 0) {
            throw new ExceptionXML("Error when reading file: The delivery duration can't be negative");
        }

        Delivery deliveryPoint = new Delivery(delivery, deliveryDuration);
        Pickup pickupPoint = new Pickup(pickup, pickupDuration);

        return new Request(pickupPoint, deliveryPoint);


    }

    private static Depot createDepot(Element depot, Map<Long, Intersection> intersections) throws ExceptionXML, ParseException {
        Long intersectionID = Long.parseLong(depot.getAttribute("address"));
        Intersection intersection = intersections.get(intersectionID);
        if(intersection == null){
            throw new ExceptionXML("Error when reading file : The depot address doesn't match with the current city map");
        }

        Date departureTime = null;
        try {
            departureTime = sdf.parse(depot.getAttribute("departureTime"));
        } catch (ParseException e) {
            throw new ExceptionXML("Error when reading file : Wrong departureTime format");
        }

        return new Depot(intersection, departureTime);
    }

    private static Element getRootElement() throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        File xml = XMLfileOpener.getInstance().open(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(xml);
        return document.getDocumentElement();
    }



}
