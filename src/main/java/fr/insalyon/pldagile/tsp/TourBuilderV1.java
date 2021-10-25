package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TourBuilderV1 { //TODO Implement TourBuilder

    //TODO Rewrite
    public List<Long> buildTour(PlanningRequest planningRequest, CityMap cityMap) {
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);
        System.out.println("CityMapGraph vertices = " + cityMapGraph.getNbVertices());
        System.out.println(simulatedAnnealing.getStepsIntersectionId());

        Map<Long,Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
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

        ArrayList<Long> intersectionSteps = new ArrayList<>(simulatedAnnealing.getStepsIntersectionId());
        Long previousIntersection = intersectionSteps.get(0);

        for (Long id : intersectionSteps.subList(1,intersectionSteps.size()-1)) {

            Dijkstra bestPathsFromOrigin = bestPaths.get(previousIntersection);
//            int currentIndex = intersectionSteps.indexOf(id);
//
            List<Long> firstTravel = bestPathsFromOrigin.getShortestPath(id);
//            firstTravel.forEach(travelId -> System.out.println("First Id = " + travelId));
            System.out.println(firstTravel.toString());
            tourIntersections.addAll(firstTravel);
            previousIntersection = id;



//            Dijkstra dijkstraSecondTravel = new Dijkstra(cityMapGraph,pickup.getIntersection().getId());
//            List<Long> secondTravel = dijkstraSecondTravel.getShortestPath(delivery.getIntersection().getId());
////            firstTravel.forEach(id -> System.out.println("Last Id = " + id));
//            tourIntersections.addAll(firstTravel);
//            tourIntersections.addAll(secondTravel);
        }

        return tourIntersections;
    }

}