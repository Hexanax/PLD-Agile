package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * WARNING : No longer used
 * Class used to compute the tour in a non-optimal way by taking the requests
 * of the planning request in an ordered way from pair to pair and
 * by making dijskra between each address composing these requests.
 */
public class TourBuilderV1 {

    /**
     * Allows to browse the requests of the planning and to perform dijskra
     * between the intersections composing the addresses in an even way.
     * @param planningRequest the planning request
     * @param cityMap the city map
     * @return List of intersections id to visit in order
     */
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

        return tourIntersections;
    }

}