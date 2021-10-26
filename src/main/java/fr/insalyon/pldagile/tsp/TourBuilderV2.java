package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;
import javafx.util.Pair;

import java.util.*;

public class TourBuilderV2 {

    public Tour buildTour(PlanningRequest planningRequest, CityMap cityMap) {

        //List of ordered intersections to visit during the tour
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);

        //SimulatedAnnealing runs on the planningRequest applied to the graph to find an optimized tour
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

        Map<Long,Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();

        //ArrayList of ordered **STEPS** to visit, computed by the simulatedAnnealing algorithm.
        ArrayList<Long> intersectionSteps = new ArrayList<>(simulatedAnnealing.getStepsIntersectionId());

        //Iterate over all intersectionSteps to compute the full tour, with intermediary intersections
        //in the _tourIntersections_ List

        Long previousIntersection = intersectionSteps.get(0);
        for (Long destinationId : intersectionSteps.subList(1,intersectionSteps.size())) {

            Dijkstra bestPathsFromOrigin = bestPaths.get(previousIntersection);
            List<Long> localTravel = bestPathsFromOrigin.getShortestPath(destinationId);

            //Don't add the last intersection to the tourIntersections, because it will
            //Be added as the first intersection of the next travel
            tourIntersections.addAll(localTravel.subList(0,localTravel.size()-1));
            previousIntersection = destinationId;
        }
        //We have to manually add the depot intersection to the end of the list
        tourIntersections.add(intersectionSteps.get(0));


        System.out.println(tourIntersections);


        Tour tour = new Tour(planningRequest.getRequests(),planningRequest.getDepot());
        Map<Long, Intersection> intersections = cityMap.getIntersections();
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();
        Long previous = tourIntersections.get(0);
        tour.addIntersection(intersections.get(previous));
        tourIntersections.remove(0);
        for(Long idIntersection : tourIntersections){
            Long current = idIntersection;
            Segment currentSegment = segments.get(new Pair<>(previous,current));
            tour.addSegment(currentSegment);
            previous = current;
            tour.addIntersection(intersections.get(previous));
        }

        for(Request request : planningRequest.getRequests()){
            tour.addPickupTime(request.getPickup().getDuration());
            tour.addDeliveryTime(request.getDelivery().getDuration());
        }

        return tour;



    }

    public Map<Long, List<Pair<Address, Long>>> buildSpecificIntersections(Tour tour){
        List<Request> requests = tour.getRequests();
        List<Intersection> intersections = tour.getIntersections();
        List<Segment> segments = tour.getPath();
        Map<Long, List<Pair<Address,Long>>> specificIntersections = new HashMap<Long, List<Pair<Address, Long>>>();
        for(Request request : requests){
            Pickup origin = request.getPickup();
            Delivery destination = request.getDelivery();
            Long id = request.getId();

            Long originID = origin.getIntersection().getId();
            boolean exists = specificIntersections.containsKey(originID);
            if(exists){
                specificIntersections.get(originID).add(new Pair<>(origin,id));
            } else {
                specificIntersections.put(originID, new ArrayList<Pair<Address,Long>>());
                specificIntersections.get(originID).add(new Pair<>(origin,id));
            }

            Long destinationId = destination.getIntersection().getId();
            exists = specificIntersections.containsKey(destinationId);
            if(exists){
                specificIntersections.get(destinationId).add(new Pair<>(destination,id));
            } else {
                specificIntersections.put(destinationId, new ArrayList<Pair<Address,Long>>());
                specificIntersections.get(destinationId).add(new Pair<>(destination,id));
            }
        }
        Depot depot = tour.getDepot();
        Long depotID = depot.getIntersection().getId();
        boolean exists = specificIntersections.containsKey(depotID);
        if(exists){
            specificIntersections.get(depotID).add(new Pair<>(depot,null));
        } else {
            specificIntersections.put(depotID, new ArrayList<Pair<Address,Long>>());
            specificIntersections.get(depotID).add(new Pair<>(depot,null));
        }

        return specificIntersections;
    }

}