package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.lang.model.element.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLDeserializerTest {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");
    private File testMapFile;

    @BeforeEach
    void setup() throws Exception {
        testMapFile = new File(getClass().getClassLoader().getResource("xml/testMap.xml").toURI());

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
        File inputFile = new File(getClass().getClassLoader().getResource("xml/testMap_invalid_1.xml").toURI());
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

}