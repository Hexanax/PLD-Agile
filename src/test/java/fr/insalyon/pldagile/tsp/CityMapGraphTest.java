package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityMapGraphTest {
    CityMap cityMap;
    CityMapGraph cityMapGraph;

    @BeforeEach
    void setup() throws ExceptionXML {
        Intersection[] intersections = {
                new Intersection(1L, new Coordinates(4, 5)),
                new Intersection(2L, new Coordinates(5, 8)),
                new Intersection(3L, new Coordinates(6, 9)),
                new Intersection(4L, new Coordinates(6, 3)),
                new Intersection(5L, new Coordinates(2, 7)),
                new Intersection(6L, new Coordinates(3, 3)),
                new Intersection(7L, new Coordinates(5, 1)),
                new Intersection(8L, new Coordinates(1, 3)),

        };
        Segment[] segments = {
                new Segment("1to2", 3, intersections[0], intersections[1]),
                new Segment("2to3", 1, intersections[1], intersections[2]),
                new Segment("4to3", 10, intersections[3], intersections[2]),
                new Segment("4to1", 3, intersections[3], intersections[0]),
                new Segment("6to5", 4.5, intersections[5], intersections[4]),
                new Segment("7to4", 2.5, intersections[6], intersections[3]),
        };
        cityMap = new CityMap();
        for (Intersection i : intersections) {
            cityMap.add(i);
        }
        for (Segment s : segments) {
            cityMap.add(s);
        }
        cityMapGraph = new CityMapGraph(cityMap);
    }


    @Test
    @DisplayName("Test getNbVertices works")
    public void test_getNbVertices() {
        int nbVertices = cityMapGraph.getNbVertices();
        int expectedNbVertices = 8;
        assertEquals(expectedNbVertices, nbVertices);
    }

    @Test
    @DisplayName("Test getCost works")
    public void testGetCost() {
        class TestCase {
            final Double expectedResult;
            final Long originId;
            final Long destinationId;

            public TestCase(Double expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        }
        TestCase[] tests = {new TestCase(3D, 1L, 2L),
                new TestCase(-1D, 1L, 3L),
                new TestCase(1D, 2L, 3L),
        };
        for (TestCase tc : tests) {
            Double actualResult = cityMapGraph.getCost(tc.originId, tc.destinationId);
            assertEquals(tc.expectedResult, actualResult);
        }
    }

    @Test
    @DisplayName("Test isArc works")
    public void test_isArc() {
        class TestCase {
            final boolean expectedResult;
            final Long originId;
            final Long destinationId;

            public TestCase(boolean expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        }
        TestCase[] tests = {
                new TestCase(true, 1L, 2L),
                new TestCase(false, 1L, 3L),
        };
        for (TestCase tc : tests) {
            boolean actualResult = cityMapGraph.isArc(tc.originId, tc.destinationId);
            assertEquals(tc.expectedResult, actualResult);
        }
    }

    @Test
    @DisplayName("Test shortest path cost works")
    public void test_getShortestPathCost() {
        class TestCase {
            final Double expectedResult;
            final Long originId;
            final Long destinationId;

            public TestCase(Double expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        }
        TestCase[] tests = {
                new TestCase(3D, 1L, 2L),
                new TestCase(Double.POSITIVE_INFINITY, 6L, 8L),
                new TestCase(4D, 1L, 3L),
                new TestCase(7D, 4L, 3L),
        };
        for (TestCase tc : tests) {
            Double actualResult = cityMapGraph.getShortestPathCost(tc.originId, tc.destinationId);
            assertEquals(tc.expectedResult, actualResult);
        }
    }

    @Test
    @DisplayName("Test get shortest path works")
    public void test_getShortestPath() {
        class TestCase {
            final List<Long> expectedResult;
            final Long originId;
            final Long destinationId;

            public TestCase(List<Long> expectedResult, Long originId, Long destinationId) {
                this.expectedResult = expectedResult;
                this.originId = originId;
                this.destinationId = destinationId;
            }
        }
        TestCase[] tests = {
                new TestCase(List.of(1L, 2L), 1L, 2L)
        };
        for (TestCase tc : tests) {
            ArrayList<Long> actualResult = (ArrayList<Long>) cityMapGraph.getShortestPath(tc.originId, tc.destinationId);
            assertEquals(tc.expectedResult.size(), actualResult.size());
            assertEquals(tc.expectedResult.get(0), actualResult.get(0));
            assertEquals(tc.expectedResult.get(1), actualResult.get(1));
        }
    }

}
