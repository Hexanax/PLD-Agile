package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.helpers.FakeIntersectionProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanningRequestTest {
    private List<Request> requests;
    private List<Intersection> intersections;
    private Depot depot;
    private PlanningRequest planningRequest;

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:m:s");

    @BeforeEach
    public void firstSetup() throws ParseException {
        intersections = FakeIntersectionProvider.getFakeIntersections();
        this.requests = List.of(
                new Request(
                        new Pickup(intersections.get(1) , 420),
                        new Delivery(intersections.get(4), 600)
                ),
                new Request(
                        new Pickup(intersections.get(3) , 420),
                        new Delivery(intersections.get(6), 480)
                ),
                new Request(
                        new Pickup(intersections.get(1) , 420),
                        new Delivery(intersections.get(6), 600)
                )
                );
        Date departureTime = simpleDateFormat.parse("8:0:0");
        this.depot = new Depot(this.intersections.get(1),departureTime);
        this.planningRequest = new PlanningRequest(requests, depot);

    }

    @Test
    @DisplayName("Test getRequests works")
    public void test_getRequests(){
        List<Request> expectedRequests = this.requests;
        List<Request> actualRequests = planningRequest.getRequests();
        assertEquals(expectedRequests,actualRequests);
    }

    @Test
    @DisplayName("Test getDepot works")
    public void test_getDepot(){
        Depot expectedRequests = this.depot;
        Depot actualRequests = planningRequest.getDepot();
        assertEquals(expectedRequests,actualRequests);
    }

    @Test
    @DisplayName("Test addRequest works")
    public void test_addRequest(){
        this.planningRequest = new PlanningRequest();
        Request request =           new Request(
                new Pickup(intersections.get(5) , 20),
                new Delivery(intersections.get(3), 20)
        );
        int expectedLength = 1;
        this.planningRequest.add(request);
        int actualLength = this.planningRequest.getRequests().size();
        assertEquals(expectedLength,actualLength);
        assertEquals(request, this.planningRequest.getRequests().get(0));
    }
}
