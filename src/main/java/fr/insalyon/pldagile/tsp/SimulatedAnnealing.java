package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.Delivery;
import fr.insalyon.pldagile.model.Pickup;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import javafx.util.Pair;

import java.util.*;

public class SimulatedAnnealing {


    //Parameters of our Simulated Annealing algorithm
    private double temperature;
    private double coolingRate;
    private int numberOfIterations;

    //Holds all of the best paths from an originId to each intersection of the graph
    private Map<Long, Dijkstra> bestPaths;

    //our segments/intersections in form of a graph
    private CityMapGraph cityMapGraph;

    //The steps the Traveling Salesman has to visit
    private PlanningRequest planningRequest;

    private ArrayList<Pair<Long, String>> stepsIdentifiers;
    private ArrayList<Long> stepsIntersectionId;

    //used for the revert function
    private ArrayList<Pair<Long, String>> oldStepsIdentifiers;
    private ArrayList<Long> oldIntersectionIds;



    public SimulatedAnnealing(PlanningRequest planningRequest, CityMapGraph cityMapGraph) {
        this.planningRequest = planningRequest;
        this.cityMapGraph = cityMapGraph;
        this.bestPaths = new HashMap<>();
        this.stepsIdentifiers = new ArrayList<>();
        this.stepsIntersectionId = new ArrayList<>();
        computeAllShortestPaths();
        runSimulatedAnnealing(25.0,1000,0.99);
    }

    public void computeAllShortestPaths() {

        //compute Dijkstra from the depot intersection
        Dijkstra dijkstraData;
        Long depotId = planningRequest.getDepot().getIntersection().getId();
        dijkstraData = new Dijkstra(cityMapGraph, depotId);

        //store the result in our hashmap
        bestPaths.put(dijkstraData.getOriginId(), dijkstraData);

        //start the travel with the depot

        stepsIdentifiers.add(new Pair(-1, "begin"));
        stepsIntersectionId.add(depotId);

        for (Request request : planningRequest.getRequests()) {

            Long requestId = request.getId();
            Pickup pickup = request.getPickup();
            Delivery delivery = request.getDelivery();

            dijkstraData = new Dijkstra(cityMapGraph, pickup.getIntersection().getId());
            bestPaths.put(dijkstraData.getOriginId(), dijkstraData);

            stepsIdentifiers.add(new Pair(requestId, "pickup"));
            stepsIntersectionId.add(dijkstraData.getOriginId());


            dijkstraData = new Dijkstra(cityMapGraph, delivery.getIntersection().getId());
            bestPaths.put(dijkstraData.getOriginId(), dijkstraData);
            stepsIdentifiers.add(new Pair(requestId, "delivery"));
            stepsIntersectionId.add(dijkstraData.getOriginId());

        }

        //end the travel with the depot
        stepsIdentifiers.add(new Pair(new Long(stepsIdentifiers.size()), "end"));
        stepsIntersectionId.add(depotId);


        //at this point, initialTravel = visitedSteps, and requests are delivered sequentially
    }

    /**
     * Inspired from https://www.baeldung.com/java-simulated-annealing-for-traveling-salesman
     * @param startingTemperature
     * @param numberOfIterations
     * @param coolingRate
     */
    public void runSimulatedAnnealing(double startingTemperature, int numberOfIterations, double coolingRate) {
        boolean swapResult;
        double bestDistance = getTotalDistance();
        double t = startingTemperature;

        System.out.println(stepsIntersectionId.toString());
        System.out.println(stepsIdentifiers.toString());
        for (int i = 0; i < numberOfIterations; i++) {
            if (t > 0.1) {
                oldStepsIdentifiers = (ArrayList<Pair<Long, String>>) stepsIdentifiers.clone();
                oldIntersectionIds = (ArrayList<Long>) stepsIntersectionId.clone();
                do {
                    int stepsSize = stepsIntersectionId.size();
                    //Generates random number between zero and stepsSize-1
                    int swapFirstIndex = 1 + (int) (Math.random() * (stepsSize-1));
                    int swapSecondIndex = 1 + (int) (Math.random() * (stepsSize-1));
                    swapResult = swapSteps(swapFirstIndex,swapSecondIndex);
                } while (swapResult == false);
                double currentDistance = getTotalDistance();
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                } else if ((Math.exp(bestDistance - currentDistance) / startingTemperature) < Math.random()) {
                    revertSwapSteps(oldStepsIdentifiers, oldIntersectionIds);
                }
            } else {
                continue;
            }
            t*=coolingRate;


        }

        System.out.println(stepsIntersectionId.toString());
        System.out.println(stepsIdentifiers.toString());
    }

    public double getTotalDistance() {
        Double totalDistance = 0.0;
        //for all entries in the stepsLocation
        //We get the shortest path cost between entry i and i+1
        for (int i = 0; i < stepsIntersectionId.size() - 1; i++) {
            Double localDistance = bestPaths.get(stepsIntersectionId.get(i)).getShortestPathCost(stepsIntersectionId.get(i + 1));
            totalDistance += localDistance;
        }
        return totalDistance;
    }

    /**
     * Swaps two steps in the ordered list of steps.
     * If the swap is not allowed, returns false.
     * If the swap is allowed, returns the new order.
     *
     * @return
     */
    public boolean swapSteps(int swapFirstIndex, int swapSecondIndex) {


        ArrayList<Pair<Long, String>> cloneIdentifier = (ArrayList<Pair<Long, String>>) stepsIdentifiers.clone();
        int stepsSize = stepsIntersectionId.size();
        //int swapFirstIndex = 1 + (int) (Math.random() * stepsSize);
        //int swapSecondIndex = 1 + (int) (Math.random() * stepsSize);
        if (swapFirstIndex == 0 || swapFirstIndex == stepsSize - 1 || swapSecondIndex == 0 || swapSecondIndex == stepsSize - 1) {
//            System.out.println("cant swap depot");
            return false;
        }
        Pair<Long, String> temp = cloneIdentifier.get(swapFirstIndex);
        cloneIdentifier.set(swapFirstIndex, cloneIdentifier.get(swapSecondIndex));
        cloneIdentifier.set(swapSecondIndex, temp);

        //Check if the swap was allowed
        //get the details of the object at index first
        Pair<Long, String> identifierObject = cloneIdentifier.get(swapFirstIndex);
        Long objectId = identifierObject.getKey();
        String objectValue = identifierObject.getValue();

        int deliveryIndex;
        int pickupIndex;
        //search for the associated delivery, it mustnt be of index inferior to the new pickup index
        if (objectValue == "pickup") {
            deliveryIndex = cloneIdentifier.indexOf(new Pair(objectId, "delivery"));
            if (deliveryIndex < swapFirstIndex) {
                return false;
            }
        }

        //search for the associated pickup, it musnt be of index superior to the new delivery index
        if (objectValue == "delivery") {
            pickupIndex = cloneIdentifier.indexOf(new Pair(objectId, "pickup"));
            if (pickupIndex > swapFirstIndex) {
                return false;
            }
        }

        identifierObject = cloneIdentifier.get(swapSecondIndex);
        objectId = identifierObject.getKey();
        objectValue = identifierObject.getValue();
        //search for the delivery
        if (objectValue == "pickup") {
            deliveryIndex = cloneIdentifier.indexOf(new Pair(objectId, "delivery"));
            if (deliveryIndex < swapSecondIndex) {
                return false;
            }
        }

        if (objectValue == "delivery") {
            pickupIndex = cloneIdentifier.indexOf(new Pair(objectId, "pickup"));
            if (pickupIndex > swapSecondIndex) {
                return false;
            }
        }

        //Once all the conditions have passed
        //We replace stepsIdentifiers
        //We swap the values in the stepsIntersectionsId
        stepsIdentifiers = cloneIdentifier;
        Long tempLocation = stepsIntersectionId.get(swapFirstIndex);
        stepsIntersectionId.set(swapFirstIndex, stepsIntersectionId.get(swapSecondIndex));
        stepsIntersectionId.set(swapSecondIndex, tempLocation);


        return true;

    }

    public void revertSwapSteps(ArrayList<Pair<Long,String>> oldStepsIdentifiers, ArrayList<Long> oldIntersectionIds) {
        stepsIdentifiers = oldStepsIdentifiers;
        stepsIntersectionId = oldIntersectionIds;
    }

    public ArrayList<Pair<Long, String>> getStepsIdentifiers() {
        return stepsIdentifiers;
    }

    public ArrayList<Long> getStepsIntersectionId() {
        return stepsIntersectionId;
    }

    private Double getDistance() {
        return 0.0;
    }

    public Map<Long, Dijkstra> getBestPaths() {
        return bestPaths;
    }

}
