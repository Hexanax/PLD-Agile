package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLDeserializerTest {

    @Test
    public void loadTest() throws Exception {

        XMLDeserializer xmlDeserializer = new XMLDeserializer();
        CityMap citymap = new CityMap();
        xmlDeserializer.load(citymap);
        assertEquals(citymap.getIntersections().size(),7);
        assertEquals(citymap.getSegments().size(),7);
        Intersection intersection1 = new Intersection(1L,new Coordinates(45.5,4.8));
        Intersection intersection4 = new Intersection(4L, new Coordinates(45.5,5));
        Segment segment14 = new Segment("Boulevard Belateche",15590,intersection4,intersection1);
        assertEquals(citymap.getIntersections().get(1L),intersection1);
        assertEquals(citymap.getIntersections().get(4L),intersection4);
        assertEquals(citymap.getSegments().get(7L),segment14);

    }

}