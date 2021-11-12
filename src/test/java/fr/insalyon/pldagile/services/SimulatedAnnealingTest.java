package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.services.CityMapGraph;
import fr.insalyon.pldagile.services.SimulatedAnnealing;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedAnnealingTest {

    private CityMapGraph cityMapGraph;
    private PlanningRequest planningRequest;
    private CityMap cityMap;

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");


    @BeforeEach
    void setup() throws Exception {

        cityMap = FakeCityMapProvider.getSmallMap();

        // Request creation
        Request[] requests = {
                new Request(
                        new Pickup(cityMap.getIntersection(2L), 420),
                        new Delivery(cityMap.getIntersection(5L), 600)
                ),
                new Request(
                        new Pickup(cityMap.getIntersection(4L), 420),
                        new Delivery(cityMap.getIntersection(6L), 480)
                ),
                new Request(
                        new Pickup(cityMap.getIntersection(3L), 420),
                        new Delivery(cityMap.getIntersection(4L), 600)
                ),

        };
        Date departureTime = simpleDateFormat.parse("8:0:0");
        Depot depot = new Depot(cityMap.getIntersection(1L), departureTime);
        Long i = 0L;
        for(Request request : requests){
             request.setId(i);
             i++;
        }

        planningRequest = new PlanningRequest(List.of(requests), depot);


        cityMapGraph = new CityMapGraph(cityMap);


    }

    /**
     * Testing the shortest path from one intersection to another
     * using 1L as an origin
     */
    @Test
    @DisplayName("computeAllShortestPaths test")
    void computeAllShortestPaths() {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);
        Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
        Dijkstra bestPathsFromOne = bestPaths.get(1L);
        Map<Long, Double> distancesFromOrigin = bestPathsFromOne.getDistancesFromOrigin();
        assertEquals(3,distancesFromOrigin.get(2L));
        assertEquals(9,distancesFromOrigin.get(3L));
        assertEquals(19,distancesFromOrigin.get(4L));
        assertEquals(4.5,distancesFromOrigin.get(5L));
        assertEquals(7,distancesFromOrigin.get(6L));

    }

    @Test
    @DisplayName("runSimulatedAnnealing test")
    void runSimulatedAnnealing() {

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);
        boolean isFullyComputed = simulatedAnnealing.runSimulatedAnnealing(false,false);

        ArrayList<Pair<Long, String>> expectedTestIdentifiers = new ArrayList<>();
        expectedTestIdentifiers.add(new Pair<Long, String>(-1L,"begin"));
        expectedTestIdentifiers.add(new Pair<Long, String>(0L,"pickup"));
        expectedTestIdentifiers.add(new Pair<Long, String>(1L,"pickup"));
        expectedTestIdentifiers.add(new Pair<Long, String>(0L,"delivery"));
        expectedTestIdentifiers.add(new Pair<Long, String>(1L,"delivery"));
        expectedTestIdentifiers.add(new Pair<Long, String>(2L,"pickup"));
        expectedTestIdentifiers.add(new Pair<Long, String>(2L,"delivery"));
        expectedTestIdentifiers.add(new Pair<Long, String>(-2L,"end"));

        assertEquals(expectedTestIdentifiers,simulatedAnnealing.getStepsIdentifiers());
        assertEquals(isFullyComputed,true);

    }

    /**
     * Testing the total distance computation
     */
    @Test
    @DisplayName("getTotalDistance test")
    void getTotalDistance() {
        class TestCase {
            final Double expectedResult;
            SimulatedAnnealing simulatedAnnealing;

            public TestCase(Double expectedResult) {
                this.expectedResult = expectedResult;
                this.simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);
            }
        }

        TestCase test = new TestCase(45.0);
        test.simulatedAnnealing.runSimulatedAnnealing(false,false);
        // /!\ Could have random results due to the random nature of the algorithm
        Double actualResult = test.simulatedAnnealing.getTotalDistance();
        assertEquals(test.expectedResult, actualResult);
    }

    /**
     * Testing the swapSteps function
     */
    @Test
    @DisplayName("swapStepsTest")
    public void swapSteps() {

        class TestCase {
            final boolean expectedResult;
            final SimulatedAnnealing simulatedAnnealing;
            final int swapFirstIndex;
            final int swapSecondIndex;

            public TestCase(boolean expectedResult, int swapFirstIndex, int swapSecondIndex) {
                this.expectedResult = expectedResult;
                this.simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);
                this.swapFirstIndex = swapFirstIndex;
                this.swapSecondIndex = swapSecondIndex;

            }
        }
        TestCase[] tests = {
                //False because swap beginning depot
                new TestCase(false, 0, 1),
                //False because can't swap ending depot
                new TestCase(false, 3, 7),
                //False because can't swap pickup1 and delivery1
                new TestCase(false, 1, 2),
                //False because can't have delivery2 before pickup2
                new TestCase(false, 3, 4),
                //True because we can swap delivery1 and pickup2
                new TestCase(true, 2, 3),
                //True because we can swap delivery1 with pickup3
                new TestCase(true, 5, 2),
        };
        for (TestCase tc : tests) {
            boolean actualResult = tc.simulatedAnnealing.swapSteps(tc.swapFirstIndex, tc.swapSecondIndex);

            assertEquals(tc.expectedResult, actualResult);
        }
    }

    /**
     * Testing the revertSwapSteps function
     */
    @Test
    @DisplayName("revertSwapStepsTest")
    void revertSwapSteps() {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);

        ArrayList<Pair<Long, String>> testCopyOfIdentifiers = (ArrayList<Pair<Long, String>>) simulatedAnnealing.getStepsIdentifiers().clone();
        ArrayList<Long> testCopyOfIntersectionId = (ArrayList<Long>) simulatedAnnealing.getStepsIntersectionId().clone();

        simulatedAnnealing.swapSteps(2, 3);

        assertNotEquals(simulatedAnnealing.getStepsIdentifiers(), testCopyOfIdentifiers);
        assertNotEquals(simulatedAnnealing.getStepsIntersectionId(), testCopyOfIntersectionId);
        simulatedAnnealing.revertSwapSteps(testCopyOfIdentifiers, testCopyOfIntersectionId);
        assertEquals(simulatedAnnealing.getStepsIdentifiers(), testCopyOfIdentifiers);
        assertEquals(simulatedAnnealing.getStepsIntersectionId(), testCopyOfIntersectionId);

    }
}