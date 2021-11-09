package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.*;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedAnnealingTest {

    private CityMapGraph cityMapGraph;
    private PlanningRequest planningRequest;

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");


    @BeforeEach
    void setup() throws Exception{

        CityMap cityMap = FakeCityMapProvider.getMediumMap();

        // Request creation
        Request[] requests = {
                new Request(
                        new Pickup(cityMap.getIntersection(2L) , 420),
                        new Delivery(cityMap.getIntersection(5L), 600)
                ),
                new Request(
                        new Pickup(cityMap.getIntersection(4L) , 420),
                        new Delivery(cityMap.getIntersection(7L), 480)
                ),
                new Request(
                        new Pickup(cityMap.getIntersection(2L) , 420),
                        new Delivery(cityMap.getIntersection(7L), 600)
                ),

        };
        Date departureTime = simpleDateFormat.parse("8:0:0");
        Depot depot = new Depot(cityMap.getIntersection(1L),departureTime);

        planningRequest = new PlanningRequest(List.of(requests), depot);


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
            final Double expectedResult;
            final SimulatedAnnealing simulatedAnnealing;

            public TestCase(Double expectedResult) throws InterruptedException {
                this.expectedResult = expectedResult;
                this.simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

            }
        }

        TestCase test = new TestCase(325.0);
        // /!\ Could have random results due to the random nature of the algorithm
        Double actualResult = test.simulatedAnnealing.getTotalDistance();
        assertEquals(test.expectedResult,actualResult);
    }

    /**
     * Disabled because the swaps are done when calling simulatedAnnealing Constructor
     */
    @Disabled
    @Test
    @DisplayName("swapStepsTest")
    public void swapSteps() {

        class TestCase {
            final boolean expectedResult;
            final SimulatedAnnealing simulatedAnnealing;
            final int swapFirstIndex;
            final int swapSecondIndex;

            public TestCase(boolean expectedResult, int swapFirstIndex, int swapSecondIndex) throws InterruptedException {
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
            //////System.out.println(tc.simulatedAnnealing.getStepsIdentifiers());
            boolean actualResult = tc.simulatedAnnealing.swapSteps(tc.swapFirstIndex, tc.swapSecondIndex);
            //////System.out.println(actualResult);

            assertEquals(tc.expectedResult, actualResult);
        }


    }

    @Test
    void revertSwapSteps() throws InterruptedException {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);

        ArrayList<Pair<Long, String>> testCopyOfIdentifiers = (ArrayList<Pair<Long, String>>) simulatedAnnealing.getStepsIdentifiers().clone();
        ArrayList<Long> testCopyOfIntersectionId = (ArrayList<Long>) simulatedAnnealing.getStepsIntersectionId().clone();

        simulatedAnnealing.swapSteps(2,3);

        assertNotEquals(simulatedAnnealing.getStepsIdentifiers(), testCopyOfIdentifiers);
        assertNotEquals(simulatedAnnealing.getStepsIntersectionId(), testCopyOfIntersectionId);
        simulatedAnnealing.revertSwapSteps(testCopyOfIdentifiers,testCopyOfIntersectionId);
        assertEquals(simulatedAnnealing.getStepsIdentifiers(), testCopyOfIdentifiers);
        assertEquals(simulatedAnnealing.getStepsIntersectionId(), testCopyOfIntersectionId);

    }
}