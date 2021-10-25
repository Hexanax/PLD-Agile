package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.xml.ExceptionXML;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedAnnealingTest {

    private CityMap cityMap;
    private CityMapGraph cityMapGraph;
    private PlanningRequest planningRequest;
    private SimulatedAnnealing simulatedAnnealing;

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");

//    @BeforeEach
//    void setup() throws ExceptionXML {
//        Intersection[] intersections = {
//                new Intersection(1L, new Coordinates(4, 5)),
//                new Intersection(2L, new Coordinates(5, 8)),
//                new Intersection(3L, new Coordinates(6, 9)),
//                new Intersection(4L, new Coordinates(6, 3)),
//                new Intersection(5L, new Coordinates(2, 7)),
//                new Intersection(6L, new Coordinates(3, 3)),
//                new Intersection(7L, new Coordinates(5, 1)),
//                new Intersection(8L, new Coordinates(1, 3)),
//
//        };
//        Segment[] segments = {
//                new Segment("1to2", 3, intersections[0], intersections[1]),
//                new Segment("2to3", 7, intersections[1], intersections[2]),
//                new Segment("3to4", 10, intersections[2], intersections[3]),
//                new Segment("4to1", 3, intersections[3], intersections[0]),
//                new Segment("1to5", 4.5, intersections[0], intersections[4]),
//                new Segment("5to6", 2.5, intersections[4], intersections[5]),
//                new Segment("6to3", 2.0, intersections[5], intersections[2]),
//
//        };
//
//        Request[] requests = {
//                new Request(
//                        new Pickup(intersections[0],100),
//                        new Delivery(intersections[2],120)
//                ),
//                new Request(
//                        new Pickup(intersections[0],100),
//                        new Delivery(intersections[5],150)
//                )
//        };
//
//        cityMap = new CityMap();
//        for (Intersection i : intersections) {
//            cityMap.add(i);
//        }
//        for (Segment s : segments) {
//            cityMap.add(s);
//        }
//        cityMapGraph = new CityMapGraph(cityMap);
//
//        PlanningRequest planningRequest = new PlanningRequest();
//        planningRequest.add(new Depot(intersections[0],new Date(simpleDateFormat.parse("8:0:0")))));
//    }

    @BeforeEach
    void setup() throws Exception{

        //Load map & requests from file
        File testMapFile = new File(getClass().getClassLoader().getResource("xml/testMap.xml").toURI());
        File testRequestsFile = new File(getClass().getClassLoader().getResource("xml/testRequests.xml").toURI());

        cityMap = new CityMap();
        planningRequest = new PlanningRequest();

        Date departureTime = simpleDateFormat.parse("8:0:0");
        Intersection intersectionDepot = new Intersection(1L, new Coordinates(45.5,4.8));
        Depot depot = new Depot(intersectionDepot,departureTime);

        XMLDeserializer.load(cityMap, testMapFile);
        XMLDeserializer.load(planningRequest, cityMap, testRequestsFile);

        cityMapGraph= new CityMapGraph(cityMap);


    }

    @Test
    void computeAllShortestPaths() {
    }

    @Test
    void runSimulatedAnnealing() {
    }

    @Test
    void getTotalDistance() {
        class TestCase {
            Double expectedResult;
            SimulatedAnnealing simulatedAnnealing;

            public TestCase(Double expectedResult) {
                this.expectedResult = expectedResult;
                this.simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

            }
        }

        TestCase test = new TestCase(325.0);
        //! actuellement ça peut avoir des retours aleatoires dû a l'algo...
        Double actualResult = test.simulatedAnnealing.getTotalDistance();
        assertEquals(test.expectedResult,actualResult);
    }

    /**
     * Disabled because the swaps are already done at init  -
     */
    @Disabled
    @Test
    @DisplayName("swapStepsTest")
    public void swapSteps() {

        class TestCase {
            boolean expectedResult;
            SimulatedAnnealing simulatedAnnealing;
            int swapFirstIndex;
            int swapSecondIndex;

            public TestCase(boolean expectedResult, int swapFirstIndex, int swapSecondIndex) {
                this.expectedResult = expectedResult;
                this.simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);
                this.swapFirstIndex=swapFirstIndex;
                this.swapSecondIndex=swapSecondIndex;

            }
        }

        TestCase[] tests = {
                //False because swap beginning depot
                new TestCase(false, 0,1),
                //False because can't swap ending depot
                new TestCase(false, 3,5),
                //False because can't swap pickup1 and delivery1
                new TestCase(false, 1,2),
                //False because can't have delivery2 before pickup2
                new TestCase(false, 3,4),
                //True because we can swap delivery1 and pickup2
                new TestCase(true, 2,3),
                //False because we can't have delivery1 before pickup1
                new TestCase(false, 2,4),
        };

        for (TestCase tc : tests) {
            System.out.println(tc.simulatedAnnealing.getStepsIdentifiers());
            boolean actualResult = tc.simulatedAnnealing.swapSteps(tc.swapFirstIndex, tc.swapSecondIndex);
            System.out.println(actualResult);

            assertEquals(tc.expectedResult, actualResult);
        }


    }

    @Test
    void revertSwapSteps() {
        simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

        ArrayList<Pair<Long, String>> testCopyOfIdentifiers = (ArrayList<Pair<Long, String>>) simulatedAnnealing.getStepsIdentifiers().clone();
        ArrayList<Long> testCopyOfIntersectionId = (ArrayList<Long>) simulatedAnnealing.getStepsIntersectionId().clone();

        simulatedAnnealing.swapSteps(2,3);

        assertFalse(simulatedAnnealing.getStepsIdentifiers().equals(testCopyOfIdentifiers));
        assertFalse(simulatedAnnealing.getStepsIntersectionId().equals(testCopyOfIntersectionId));
        simulatedAnnealing.revertSwapSteps(testCopyOfIdentifiers,testCopyOfIntersectionId);
        assertTrue(simulatedAnnealing.getStepsIdentifiers().equals(testCopyOfIdentifiers));
        assertTrue(simulatedAnnealing.getStepsIntersectionId().equals(testCopyOfIntersectionId));

    }
}