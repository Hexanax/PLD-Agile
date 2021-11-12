package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.helpers.FakeIntersectionProvider;
import fr.insalyon.pldagile.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLDeserializerTest {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");
    CityMap cityMap;

    @BeforeEach
    void setup() throws Exception {
        cityMap = FakeCityMapProvider.getSmallMap();
    }

    @Test
    public void loadCityMap_NullFileException() throws Exception {
        File inputFile = null;
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.FILE_NULL_ERROR;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_WrongFormatException() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testXML_invalid_1.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.FILE_WRONG_FORMAT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_INVALID_ID() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_2.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.INVALID_ID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_INCORRECT_DESTINATION_ID() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_3.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.INCORRECT_DESTINATION_ID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_UNKNOWN_INTERSECTION_BadDestination() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_4.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.UNKNOWN_INTERSECTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_UNKNOWN_INTERSECTION_BadOrigin() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_5.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.UNKNOWN_INTERSECTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_WRONG_ORIGIN_ID() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_6.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.WRONG_ORIGIN_ID;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_WRONG_LENGTH() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_7.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(inputFile);
        });
        String expectedMessage = ExceptionXML.WRONG_LENGTH;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadCityMap_SUCCESS() throws Exception {
        CityMap expected = FakeCityMapProvider.getMediumMap();
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap.xml").toURI());
        CityMap actual = XMLDeserializer.load(inputFile);

        assertEquals(expected.getIntersections(), actual.getIntersections());
        assertEquals(expected.getSegments().size(), actual.getSegments().size());

        assertEquals(expected.getIntersections().size(), actual.getIntersections().size());
        assertEquals(expected.getSegments().size(), actual.getSegments().size());

        assertEquals(expected.getIntersections().get(1L).getId(), actual.getIntersections().get(1L).getId());
        assertEquals(expected.getIntersections().get(4L).getId(), actual.getIntersections().get(4L).getId());
        assertEquals(expected.getIntersections().get(1L).getCoordinates().getLatitude(),
                actual.getIntersections().get(1L).getCoordinates().getLatitude());
        assertEquals(expected.getIntersections().get(1L).getCoordinates().getLongitude(),
                actual.getIntersections().get(1L).getCoordinates().getLongitude());
    }

    @Test
    public void loadRequests_FILE_NULL_ERROR() throws Exception {
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, null);
        });
        String expectedMessage = ExceptionXML.FILE_NULL_ERROR;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_FILE_WRONG_FORMAT() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testXML_invalid_1.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.FILE_WRONG_FORMAT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_UNDEFINED_DEPOT() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_1.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.UNDEFINED_DEPOT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_DEPOT_CITY_MAP_MISMATCH() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_2.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.DEPOT_CITY_MAP_MISMATCH;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_WRONG_DEPARTURE_TIME_FORMAT() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_3.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.WRONG_DEPARTURE_TIME_FORMAT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_PICKUP_SAME_AS_DELIVERY() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_4.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.PICKUP_SAME_AS_DELIVERY;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_PICKUP_CITY_MAP_MISMATCH() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_5.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.PICKUP_CITY_MAP_MISMATCH;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_DELIVERY_CITY_MAP_MISMATCH() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_6.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.DELIVERY_CITY_MAP_MISMATCH;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_NEGATIVE_PICKUP_DURATION() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_7.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.NEGATIVE_PICKUP_DURATION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_NEGATIVE_DELIVERY_DURATION() throws Exception {
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest_invalid_8.xml").toURI());
        Exception exception = assertThrows(ExceptionXML.class, () -> {
            XMLDeserializer.load(cityMap, inputFile);
        });
        String expectedMessage = ExceptionXML.NEGATIVE_DELIVERY_DURATION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void loadRequests_SUCCESS() throws Exception {
        List<Intersection> intersections = FakeIntersectionProvider.getSmallFakeIntersections();
        Date departureTime = simpleDateFormat.parse("8:0:0");
        Request r = new Request(new Pickup(intersections.get(1), 180), new Delivery(intersections.get(2), 240));
        r.setId(0L);
        PlanningRequest expected = new PlanningRequest(List.of(r), new Depot(intersections.get(0), departureTime));

        File inputFile = new File(getClass().getClassLoader().getResource("xml/testRequest.xml").toURI());
        PlanningRequest actual = XMLDeserializer.load(cityMap, inputFile);

        assertEquals(expected, actual);
    }
}