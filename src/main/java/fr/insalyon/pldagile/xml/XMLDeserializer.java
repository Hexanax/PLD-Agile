package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.*;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.text.SimpleDateFormat;

/**
 * This class allows to read XML map files and XML requests files in order to laod a city map in the application or to load a list of requests
 * This code is based on the example of the 4IF course - Object Oriented Design and AGILE software development by Mrs Solnon
 * <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
 */
public class XMLDeserializer {
    /**
     * Simple date format to get a time of a day
     */
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");

    /**
     * Open an XML file and create a city map from this file
     * Inspired from @author Mrs SOLNON <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     * @return
     */
    public static CityMap load(File xmlFile) throws ParserConfigurationException, SAXException, IOException, ExceptionXML, CloneNotSupportedException {
        Element root = getRootElement(xmlFile);
        if (root.getNodeName().equals("map")) {
            return buildFromDOMXML(root);
        } else {
            throw new ExceptionXML(ExceptionXML.FILE_WRONG_FORMAT);
        }
    }

    /**
     * Browse the XML map file to fill the city map
     * Inspired from @author Mrs SOLNON <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
     * @param rootNode the root element of the XML map file
     * @throws ExceptionXML
     */
    private static CityMap buildFromDOMXML(Element rootNode) throws ExceptionXML {
        CityMap newCityMap = new CityMap();

        //build intersections
        NodeList intersectionsList = rootNode.getElementsByTagName("intersection");
        for (int i = 0; i < intersectionsList.getLength(); i++) {
            Intersection intersection = createIntersection((Element) intersectionsList.item(i));
            newCityMap.add(intersection);
        }

        //build segments
        NodeList segmentsList = rootNode.getElementsByTagName("segment");
        for (int i = 0; i < segmentsList.getLength(); i++) {
            Segment segment = createSegment((Element) segmentsList.item(i), newCityMap.getIntersections());
            newCityMap.add(segment);
        }

        return newCityMap;
    }

    /**
     * Extract from an XML element the information to create a segment of the city map
     * @param elt the current node in the XML map file which corresponds to a segment
     * @param intersections list of the intersections of the city map
     * @return a new segment of the city map
     * @throws ExceptionXML
     */
    private static Segment createSegment(Element elt, Map<Long, Intersection> intersections) throws ExceptionXML {
        long destinationID = Long.parseLong(elt.getAttribute("destination"));
        long originID = Long.parseLong(elt.getAttribute("origin"));
        if (destinationID < 0) {
            throw new ExceptionXML(ExceptionXML.INCORRECT_DESTINATION_ID);
        }
        Intersection destination = intersections.get(destinationID);
        if (destination == null) {
            throw new ExceptionXML(ExceptionXML.UNKNOWN_INTERSECTION);
        }
        if (originID < 0) {
            throw new ExceptionXML(ExceptionXML.WRONG_ORIGIN_ID);
        }
        Intersection origin = intersections.get(originID);
        if (origin == null) {
            throw new ExceptionXML(ExceptionXML.UNKNOWN_INTERSECTION);
        }

        String name = elt.getAttribute("name");
        double length = Double.parseDouble(elt.getAttribute("length"));
        if (length <= 0) {
            throw new ExceptionXML(ExceptionXML.WRONG_LENGTH);
        }
        return new Segment(name, length, origin, destination);
    }

    /**
     * Extract from an XML element the information to create a intersection of the city map
     * @param elt the current node in the XML map file which corresponds to an intersection
     * @return a new intersection of the city map
     * @throws ExceptionXML when the id is negative
     */
    private static Intersection createIntersection(Element elt) throws ExceptionXML {
        double latitude = Double.parseDouble(elt.getAttribute("latitude"));
        double longitude = Double.parseDouble(elt.getAttribute("longitude"));
        Coordinates coordinates = new Coordinates(latitude, longitude);
        long id = Long.parseLong(elt.getAttribute("id"));
        if (id < 0) {
            throw new ExceptionXML(ExceptionXML.INVALID_ID);
        }
        return new Intersection(id, coordinates);
    }

    /**
     * Open an XML file and planning request from this file
     * Inspired from @author Mrs SOLNON <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
     * @param map the city map currently used
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     * @throws ParseException
     * @return
     */
    public static PlanningRequest load(CityMap map, File xmlFile) throws ParserConfigurationException, SAXException, IOException, ExceptionXML, ParseException {
        Element root = getRootElement(xmlFile);
        if (root.getNodeName().equals("planningRequest")) {
            return buildFromDOMXML(root, map);
        } else {
            throw new ExceptionXML(ExceptionXML.FILE_WRONG_FORMAT);
        }
    }

    /**
     * Browse the XML requests file to fill the planning request
     * Inspired from @author Mrs SOLNON <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
     * @param rootNode the root element of the XML map file
     * @param map the city map currently used
     * @throws ExceptionXML
     * @throws ParseException
     * @return
     */
    private static PlanningRequest buildFromDOMXML(Element rootNode, CityMap map) throws ExceptionXML, ParseException {
        PlanningRequest planningRequest = new PlanningRequest();

        //build the depot
        NodeList depotNodeList = rootNode.getElementsByTagName("depot");
        if (depotNodeList.getLength() != 1) {
            throw new ExceptionXML(ExceptionXML.UNDEFINED_DEPOT);
        }
        Map<Long, Intersection> intersections = map.getIntersections();
        Depot depot = createDepot((Element) depotNodeList.item(0), intersections);
        planningRequest.add(depot);

        //build requests
        NodeList requests = rootNode.getElementsByTagName("request");
        for (int i = 0; i < requests.getLength(); i++) {
            planningRequest.add(createRequest((Element) requests.item(i), intersections));
        }
        return planningRequest;
    }

    /**
     * Extract from an XML element the information to create a request of a planning
     * @param request the current node in the XML map file which corresponds to a request
     * @param intersections list of the intersections of the city map
     * @return a new request of the planning
     * @throws ExceptionXML
     */
    private static Request createRequest(Element request, Map<Long, Intersection> intersections) throws ExceptionXML {
        long pickupAddressId = Long.parseLong(request.getAttribute("pickupAddress"));
        long deliveryAddressId = Long.parseLong(request.getAttribute("deliveryAddress"));
        if (pickupAddressId == deliveryAddressId) {
            throw new ExceptionXML(ExceptionXML.PICKUP_SAME_AS_DELIVERY);
        }
        Intersection pickup = intersections.get(pickupAddressId);
        Intersection delivery = intersections.get(deliveryAddressId);
        if (pickup == null) {
            throw new ExceptionXML(ExceptionXML.PICKUP_CITY_MAP_MISMATCH);
        }
        if (delivery == null) {
            throw new ExceptionXML(ExceptionXML.DELIVERY_CITY_MAP_MISMATCH);
        }

        int pickupDuration = Integer.parseInt(request.getAttribute("pickupDuration"));
        int deliveryDuration = Integer.parseInt(request.getAttribute("deliveryDuration"));
        if (pickupDuration < 0) {
            throw new ExceptionXML(ExceptionXML.DELIVERY_CITY_MAP_MISMATCH);
        }
        if (deliveryDuration < 0) {
            throw new ExceptionXML(ExceptionXML.NEGATIVE_DELIVERY_DURATION);
        }

        Delivery deliveryPoint = new Delivery(delivery, deliveryDuration);
        Pickup pickupPoint = new Pickup(pickup, pickupDuration);

        return new Request(pickupPoint, deliveryPoint);
    }

    /**
     * Extract from an XML element the information to create the depot of a planning
     * @param depot the current node in the XML map file which corresponds to a depot
     * @param intersections list of the intersections of the city map
     * @return the depot of the planning
     * @throws ExceptionXML
     */
    private static Depot createDepot(Element depot, Map<Long, Intersection> intersections) throws ExceptionXML {
        Long intersectionId = Long.parseLong(depot.getAttribute("address"));
        Intersection intersection = intersections.get(intersectionId);
        if (intersection == null) {
            throw new ExceptionXML(ExceptionXML.DEPOT_CITY_MAP_MISMATCH);
        }
        Date departureTime;
        try {
            departureTime = simpleDateFormat.parse(depot.getAttribute("departureTime"));
        } catch (ParseException e) {
            throw new ExceptionXML(ExceptionXML.WRONG_DEPARTURE_TIME_FORMAT);
        }
        return new Depot(intersection, departureTime);
    }

    /**
     * Open an XML file and return its root element
     * Copied from @author Mrs SOLNON <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
     * @return the root element of the XML file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    private static Element getRootElement(File xmlFile) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
        if(xmlFile == null){
            throw new ExceptionXML(ExceptionXML.FILE_NULL_ERROR);
        }
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        //Initialize the Dom element
        Document document = docBuilder.parse(xmlFile);
        return document.getDocumentElement();
    }

}
