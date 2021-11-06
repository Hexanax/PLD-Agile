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

        Intersection previousIntersection = planningRequest.getDepot().getIntersection();

        for (Request request : planningRequest.getRequests()) {
            Pickup pickup = request.getPickup();
            Delivery delivery = request.getDelivery();
            Dijkstra dijkstraFirstTravel = new Dijkstra(cityMapGraph, previousIntersection.getId());
            List<Long> firstTravel = dijkstraFirstTravel.getShortestPath(pickup.getIntersection().getId());

            Dijkstra dijkstraSecondTravel = new Dijkstra(cityMapGraph,pickup.getIntersection().getId());
            List<Long> secondTravel = dijkstraSecondTravel.getShortestPath(delivery.getIntersection().getId());
            tourIntersections.addAll(firstTravel);
            tourIntersections.addAll(secondTravel);
            previousIntersection = delivery.getIntersection();
        }

        ////System.out.println("Running V1");
        return tourIntersections;
    }

}