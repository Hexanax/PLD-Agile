package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;

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
            Dijkstra dijkstraFirstTravel = new Dijkstra(cityMapGraph, previousIntersection.getId());
            List<Long> firstTravel = dijkstraFirstTravel.getShortestPath(pickup.getIntersection().getId());
//            firstTravel.forEach(id -> System.out.println("First Id = " + id));

            Dijkstra dijkstraSecondTravel = new Dijkstra(cityMapGraph,pickup.getIntersection().getId());
            List<Long> secondTravel = dijkstraSecondTravel.getShortestPath(delivery.getIntersection().getId());
//            firstTravel.forEach(id -> System.out.println("Last Id = " + id));
            tourIntersections.addAll(firstTravel);
            tourIntersections.addAll(secondTravel);
            previousIntersection = delivery.getIntersection();
        }

        return tourIntersections;
    }

}