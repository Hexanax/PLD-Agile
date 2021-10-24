package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;

import java.util.ArrayList;
import java.util.List;

public class TourBuilderV1 { //TODO Implement TourBuilder

    //TODO Rewrite
    public List<Long> buildTour(PlanningRequest planningRequest, CityMap cityMap) {
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);

        System.out.println("CityMapGraph vertices = " + cityMapGraph.getNbVertices());
        //TEST
//        long pickupId = planningRequest.getDepot().getIntersection().getId();
//        long deliveryId = planningRequest.getRequests().get(0).getPickup().getIntersection().getId();
//        System.out.println("Pickup id = " + pickupId);
//        System.out.println("Delivery id = " + deliveryId);
//        List<Long> travels = cityMapGraph.getShortestPath(pickupId, deliveryId);
//        travels.forEach(id -> System.out.println("Id = " + id));
        //------

//        for (int i = 0; i < planningRequest.getRequests().size() - 1; i++) {
//            Request firstRequest = planningRequest.getRequests().get(i);
//            Request secondRequest = planningRequest.getRequests().get(i+1);
//
//        }

        Intersection previousIntersection = planningRequest.getDepot().getIntersection();

        for (Request request : planningRequest.getRequests()) {
            Pickup pickup = request.getPickup();
            Delivery delivery = request.getDelivery();
            List<Long> firstTravel = cityMapGraph.getShortestPath(previousIntersection.getId(), pickup.getIntersection().getId());
            firstTravel.forEach(id -> System.out.println("First Id = " + id));

            List<Long> secondTravel = cityMapGraph.getShortestPath(pickup.getIntersection().getId(), delivery.getIntersection().getId());
            firstTravel.forEach(id -> System.out.println("Last Id = " + id));
            tourIntersections.addAll(firstTravel);
            tourIntersections.addAll(secondTravel);
            previousIntersection = delivery.getIntersection();
        }

        return tourIntersections;
    }

}