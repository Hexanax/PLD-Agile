package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.Dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TourBuilderV2 {

    public List<Long> buildTour(PlanningRequest planningRequest, CityMap cityMap) {

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

        System.out.println("Running V2");
        return tourIntersections;
    }

}