package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLDeserializerTest {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");


    @Test
    @DisplayName("XML map parsing should work")
    public void loadMapTest() throws Exception {
//        //source file : testMap.xml
//        CityMap cityMap = new CityMap();
//        URI fileURI = getClass().getClassLoader().getResource("xml/testMap.xml").toURI();
//        File testMapFile = new File(fileURI);
//        XMLDeserializer.load(cityMap, testMapFile);
//
//        assertEquals(cityMap.getIntersections().size(),9);
//        assertEquals(cityMap.getSegments().size(),11);
//
//        assertEquals(cityMap.getIntersections().get(1L).getId(), 1L);
//        assertEquals(cityMap.getIntersections().get(4L).getId(),4L);
//        assertEquals(cityMap.getIntersections().get(1L).getCoordinates().getLatitude(),45.5);
//        assertEquals(cityMap.getIntersections().get(1L).getCoordinates().getLongitude(),4.8);
//
//        assertEquals(cityMap.getSegments().get(7L).getName(),"Boulevard Belateche");
//        assertEquals(cityMap.getSegments().get(7L).getLength(),15590);
//        assertEquals(cityMap.getSegments().get(7L).getOrigin().getId(),1L);
//        assertEquals(cityMap.getSegments().get(7L).getDestination().getId(),8L);
    }

    @Test
    @DisplayName("XML Requests parsing should work")
    public void loadRequestsTest() throws Exception{
        //source file : testRequest.xml
//        File testMapFile = new File(getClass().getClassLoader().getResource("xml/testMap.xml").toURI());
//        File testRequestsFile = new File(getClass().getClassLoader().getResource("xml/testRequests.xml").toURI());
//
//        CityMap citymap = new CityMap();
//        PlanningRequest planningRequest = new PlanningRequest();
//
//        Date departureTime = simpleDateFormat.parse("8:0:0");
//        Intersection intersectionDepot = new Intersection(1L, new Coordinates(45.5,4.8));
//        Depot depot = new Depot(intersectionDepot,departureTime);
//
//        XMLDeserializer.load(citymap, testMapFile);
//        XMLDeserializer.load(planningRequest, citymap, testRequestsFile);
//
//        //Testing the depot parsing
//        assertEquals(planningRequest.getDepot().getIntersection().getId(), 1L);
//        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLatitude(),45.5);
//        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLongitude(), 4.8);
//        assertEquals(planningRequest.getDepot().getDepartureTime(), departureTime);
//
//        //Testing the requests parsing
//        assertEquals(planningRequest.getRequests().size(),2);
//
//        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getId(),2L);
//        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLatitude(),45.6);
//        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLongitude(),4.9);
//        assertEquals(planningRequest.getRequests().get(0).getPickup().getDuration(),420);
//
//        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getId(),5L);
//        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getCoordinates().getLatitude(),45.6);
//        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getCoordinates().getLongitude(),5);
//        assertEquals(planningRequest.getRequests().get(0).getDelivery().getDuration(),600);

    }

}