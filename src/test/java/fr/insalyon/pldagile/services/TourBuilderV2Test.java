package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.helpers.FakeCityMapProvider;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


class TourBuilderV2Test {

    private CityMapGraph cityMapGraph;
    private PlanningRequest planningRequest;
    private CityMap cityMap;
    private SimulatedAnnealing simulatedAnnealing;


    private int indexBeforePickup;
    private int indexBeforeDelivery;

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
        simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);
        simulatedAnnealing.runSimulatedAnnealing(false,false);

    }

    @Test
    @DisplayName("buildTour test")
    void buildTour() throws ExceptionCityMap {
        TourBuilderV2 tourBuilderV2 = new TourBuilderV2();
        Tour actualTour = tourBuilderV2.buildTour(planningRequest,cityMap,false,null);
        assertEquals(1680.0,actualTour.getDeliveriesDuration());
        assertEquals(1260.0,actualTour.getPickupsDuration());
        assertEquals(10.8,actualTour.getTravelsDuration());
        assertEquals(45.0,actualTour.getLength());
        assertEquals(9,actualTour.getPath().size());
    }

    @Test
    @DisplayName("deleteRequest test")
    void deleteRequest() throws ExceptionCityMap {
        TourBuilderV2 tourBuilderV2 = new TourBuilderV2();
        Tour actualTour = tourBuilderV2.buildTour(planningRequest,cityMap,false,null);
        Tour newTour = tourBuilderV2.deleteRequest(cityMap,actualTour,actualTour.getRequests().get(2L));
        assertEquals(1080.0,actualTour.getDeliveriesDuration());
        assertEquals(840.0,actualTour.getPickupsDuration());
        assertEquals(10.8,actualTour.getTravelsDuration());
        assertEquals(45.0,actualTour.getLength());
        assertEquals(9,actualTour.getPath().size());
    }

    @Test
    @DisplayName("addRequest test")
    void addRequest() throws ExceptionXML, ParseException, ExceptionCityMap {
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
        simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);
        simulatedAnnealing.runSimulatedAnnealing(false,false);
        TourBuilderV2 tourBuilderV2 = new TourBuilderV2();
        Tour actualTour = tourBuilderV2.buildTour(planningRequest,cityMap,false,null);

        Request request = new Request(
                new Pickup(cityMap.getIntersection(3L), 420),
                new Delivery(cityMap.getIntersection(4L), 600)
        );
        request.setId(2L);
//        planningRequest.add(newRequest,true);

//        Request request = planningRequest.getLastRequest();
        int index =0;
        for(Pair<Long,String> step : actualTour.getSteps()){
            if(Objects.equals(step.getKey(), 1L)){
                if(Objects.equals(step.getValue(), "pickup")){
                    indexBeforePickup = index-1;
                } else {
                    indexBeforeDelivery = index -1;
                }
            }
            index++;
        }


        actualTour.addRequest(request);
        actualTour.addStep(indexBeforePickup, new Pair<>(request.getId(),"pickup"));
        actualTour.addStep(indexBeforeDelivery, new Pair<>(request.getId(),"delivery"));

        Tour newTour = tourBuilderV2.addRequest(cityMap,actualTour,request.getId());
        assertEquals(1680.0,newTour.getDeliveriesDuration());
        assertEquals(1260.0,newTour.getPickupsDuration());
        assertEquals(10.8,newTour.getTravelsDuration());
        assertEquals(45.0,newTour.getLength());
        assertEquals(9,newTour.getPath().size());
    }

    @Test
    void computeTour() {
    }

    @Test
    void deadEndIntersection() {
    }
}