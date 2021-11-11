package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.services.CityMapGraph;
import fr.insalyon.pldagile.services.Dijkstra;
import fr.insalyon.pldagile.xml.ExceptionXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityMapGraphTest {
    CityMapGraph cityMapGraph;

    @BeforeEach
    void setup() throws ExceptionXML {
        CityMap cityMap = FakeCityMapProvider.getSmallMap();
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
                new TestCase(7D, 2L, 3L),
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
                new TestCase(9D, 1L, 3L),
                new TestCase(12D, 4L, 3L),
        };
        for (TestCase tc : tests) {
            Dijkstra dijkstra = new Dijkstra(cityMapGraph,tc.originId);
            Double actualResult = dijkstra.getDistancesFromOrigin().get(tc.destinationId);
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
                new TestCase(List.of(1L, 5L, 6L, 3L), 1L, 3L),
                new TestCase(List.of(4L, 1L, 5L), 4L, 5L),
                new TestCase(List.of(3L, 4L, 1L), 3L, 1L),

        };
        for (TestCase tc : tests) {
            Dijkstra dijkstra = new Dijkstra(cityMapGraph,tc.originId);
            ArrayList<Long> actualResult = (ArrayList<Long>) dijkstra.getShortestPath(tc.destinationId);
            assertEquals(tc.expectedResult.size(), actualResult.size());
            assertEquals(tc.expectedResult.get(0), actualResult.get(0));
            assertEquals(tc.expectedResult.get(1), actualResult.get(1));
        }
    }



}