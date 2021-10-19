package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityMapGraphTest {
    CityMap cityMap;
    CityMapGraph cityMapGraph;
    @BeforeEach
    void setup() throws ExceptionXML {
        Intersection[] intersections = {new Intersection(1L,new Coordinates(45,5)),
                                        new Intersection(2L,new Coordinates(50,8)),
                                        new Intersection(3L,new Coordinates(55,9))
        };

        Segment segment = new Segment("rue VL 25", 5.8,intersections[0], intersections[1]);

        cityMap = new CityMap();
        for (Intersection i :intersections) {
            cityMap.add(i);
        }
        cityMap.add(segment);
        cityMapGraph = new CityMapGraph(cityMap);
    }


    @Test
    @DisplayName("Test getNbVertices works")
    public void testGetNbVertices() {
        int nbVertices = cityMapGraph.getNbVertices();
        int expectedNbVertices = 3;
        assertEquals(expectedNbVertices, nbVertices);
    }

    @Test
    @DisplayName("Test getCost works")
    public void testGetCost() {
        class TestCase {
            Double expectedResult;
            Long originId;
            Long destinationId;

            public TestCase(Double expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        };
        TestCase[] tests = {new TestCase(5.8D, 1L, 2L),
                new TestCase(-1D, 1L, 3L),
                new TestCase(-1D, 2L, 3L)};
        for (TestCase tc: tests) {
            Double actualResult = cityMapGraph.getCost(tc.originId,tc.destinationId);
            assertEquals(tc.expectedResult,actualResult);
        }
    }

    @Test
    @DisplayName("Test isArc works")
    public void testIsArc() {
        class TestCase {
            boolean expectedResult;
            Long originId;
            Long destinationId;

            public TestCase(boolean expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        };
        TestCase[] tests = {new TestCase(true, 1L, 2L),
                new TestCase(false, 1L, 3L),
                new TestCase(false, 2L, 3L)};
        for (TestCase tc: tests) {
            boolean actualResult = cityMapGraph.isArc(tc.originId,tc.destinationId);
            assertEquals(tc.expectedResult,actualResult);
        }
    }
}
