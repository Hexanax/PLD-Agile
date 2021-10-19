package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLDeserializerTest {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");


    @Test
    public void loadMapTest() throws Exception {
        //source file : testMap.xml
        XMLDeserializer xmlDeserializer = new XMLDeserializer();
        CityMap citymap = new CityMap();
        xmlDeserializer.load(citymap);

        assertEquals(citymap.getIntersections().size(),7);
        assertEquals(citymap.getSegments().size(),7);

        Intersection intersection1 = new Intersection(1L,new Coordinates(45.5,4.8));
        Intersection intersection4 = new Intersection(4L, new Coordinates(45.5,5));
        Segment segment14 = new Segment("Boulevard Belateche",15590,intersection4,intersection1);

        assertEquals(citymap.getIntersections().get(1L).getId(), intersection1.getId());
        assertEquals(citymap.getIntersections().get(4L).getId(),intersection4.getId());
        assertEquals(citymap.getIntersections().get(1L).getCoordinates().getLatitude(),intersection1.getCoordinates().getLatitude());
        assertEquals(citymap.getIntersections().get(1L).getCoordinates().getLongitude(),intersection1.getCoordinates().getLongitude());

        assertEquals(citymap.getSegments().get(7L).getName(),segment14.getName());
        assertEquals(citymap.getSegments().get(7L).getLength(),segment14.getLength());
        assertEquals(citymap.getSegments().get(7L).getOrigin().getId(),intersection4.getId());
        assertEquals(citymap.getSegments().get(7L).getDestination().getId(),intersection1.getId());





    }

    @Test
    public void loadRequestsTest() throws Exception{
        //source file : testRequest.xml
        XMLDeserializer xmlDeserializer = new XMLDeserializer();

        CityMap citymap = new CityMap();
        PlanningRequest planningRequest = new PlanningRequest();

        Date departureTime = simpleDateFormat.parse("8:0:0");
        Intersection intersectionDepot = new Intersection(1L, new Coordinates(45.5,4.8));
        Depot depot = new Depot(intersectionDepot,departureTime);

        xmlDeserializer.load(citymap);
        xmlDeserializer.load(planningRequest,citymap);

        //Testing the depot parsing
        assertEquals(planningRequest.getDepot().getIntersection().getId(), intersectionDepot.getId());
        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLongitude(), intersectionDepot.getCoordinates().getLongitude());
        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLatitude(), intersectionDepot.getCoordinates().getLatitude());

        assertEquals(planningRequest.getDepot().getDepartureTime(), departureTime);

        Intersection intersecPickup = new Intersection(2L,new Coordinates(45.6,4.9));
        Intersection intersecDelivery = new Intersection(5L,new Coordinates(45.6,5));
        Pickup pickup1 = new Pickup(intersecPickup,420);
        Delivery delivery1 = new Delivery(intersecDelivery,600);
        Request request1 = new Request(pickup1,delivery1);

        //Testing the requests parsing
        assertEquals(planningRequest.getRequests().size(),2);

        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getId(),pickup1.getIntersection().getId());
        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLatitude(),pickup1.getIntersection().getCoordinates().getLatitude());
        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLongitude(),pickup1.getIntersection().getCoordinates().getLongitude());
        assertEquals(planningRequest.getRequests().get(0).getPickup().getDuration(),pickup1.getDuration());





    }

}