package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.*;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Map parsing should work")
    public void loadMapTest() throws Exception {
        //source file : testMap.xml
        XMLDeserializer xmlDeserializer = new XMLDeserializer();
        CityMap citymap = new CityMap();
        xmlDeserializer.load(citymap);

        assertEquals(citymap.getIntersections().size(),7);
        assertEquals(citymap.getSegments().size(),7);

        assertEquals(citymap.getIntersections().get(1L).getId(), 1L);
        assertEquals(citymap.getIntersections().get(4L).getId(),4L);
        assertEquals(citymap.getIntersections().get(1L).getCoordinates().getLatitude(),45.5);
        assertEquals(citymap.getIntersections().get(1L).getCoordinates().getLongitude(),4.8);

        assertEquals(citymap.getSegments().get(7L).getName(),"Boulevard Belateche");
        assertEquals(citymap.getSegments().get(7L).getLength(),15590);
        assertEquals(citymap.getSegments().get(7L).getOrigin().getId(),4L);
        assertEquals(citymap.getSegments().get(7L).getDestination().getId(),1L);





    }

    @Test
    @DisplayName("Requests parsing should work")
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
        assertEquals(planningRequest.getDepot().getIntersection().getId(), 1L);
        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLatitude(),45.5);
        assertEquals(planningRequest.getDepot().getIntersection().getCoordinates().getLongitude(), 4.8);
        assertEquals(planningRequest.getDepot().getDepartureTime(), departureTime);

        //Testing the requests parsing
        assertEquals(planningRequest.getRequests().size(),2);

        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getId(),2L);
        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLatitude(),45.6);
        assertEquals(planningRequest.getRequests().get(0).getPickup().getIntersection().getCoordinates().getLongitude(),4.9);
        assertEquals(planningRequest.getRequests().get(0).getPickup().getDuration(),420);

        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getId(),5L);
        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getCoordinates().getLatitude(),45.6);
        assertEquals(planningRequest.getRequests().get(0).getDelivery().getIntersection().getCoordinates().getLongitude(),5);
        assertEquals(planningRequest.getRequests().get(0).getDelivery().getDuration(),600);

    }

}