package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TourBuilderV2 {

    public void buildTour(PlanningRequest planningRequest, CityMap cityMap, Tour tour) {

        //List of ordered intersections to visit during the tour
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);

        //SimulatedAnnealing runs on the planningRequest applied to the graph to find an optimized tour
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(planningRequest,cityMapGraph);

        Map<Long,Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();

        //ArrayList of ordered **STEPS** to visit, computed by the simulatedAnnealing algorithm.
        ArrayList<Long> intersectionSteps = new ArrayList<>(simulatedAnnealing.getStepsIntersectionId());

        Long previousIntersection = intersectionSteps.get(0);


        //Iterate over all intersectionSteps to compute the full tour, with intermediary intersections
        //in the _tourIntersections_ List
        for (Long id : intersectionSteps.subList(1,intersectionSteps.size()-1)) {

            Dijkstra bestPathsFromOrigin = bestPaths.get(previousIntersection);
            List<Long> localTravel = bestPathsFromOrigin.getShortestPath(id);
            tourIntersections.addAll(localTravel);
            previousIntersection = id;
        }


        tour = new Tour(planningRequest.getRequests(),planningRequest.getDepot());
        Map<Long, Intersection> intersections = cityMap.getIntersections();
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();
        Long previous = tourIntersections.get(0);
        tour.addIntersection(intersections.get(previous));
        tourIntersections.remove(0);
        for(Long idIntersection : tourIntersections){
            System.out.println(intersections.get(previous));
            System.out.println(intersections.get(idIntersection));
            Long current = idIntersection;
            //if(!Objects.equals(previous, current)){ Magouille pour que ça marche
                Segment currentSegment = segments.get(new Pair<>(previous,current));
                System.out.println(currentSegment.toString());
                tour.addSegment(currentSegment);
                previous = current;
                tour.addIntersection(intersections.get(previous));
           // }
        }

        for(Request request : planningRequest.getRequests()){
            tour.addPickupTime(request.getPickup().getDuration());
            tour.addDeliveryTime(request.getDelivery().getDuration());
        }

        System.out.println("Running V2");

    }

}