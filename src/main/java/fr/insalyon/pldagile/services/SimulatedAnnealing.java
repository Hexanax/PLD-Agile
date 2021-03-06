package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.Delivery;
import fr.insalyon.pldagile.model.Pickup;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.services.CityMapGraph;
import fr.insalyon.pldagile.services.Dijkstra;
import javafx.util.Pair;

import java.util.*;

/**
 * Class used to store the result of the Simulated Annealing algorithm applied to our CityMapGraph and PlanningRequests.
 */
public class SimulatedAnnealing {


    public static final int MAXIMUM_TIME = 2000;
    //Parameters of our Simulated Annealing algorithm
    private double temperature = 1000;
    private final double coolingRate = 0.98;
    private double finalTemperature = 0.1;
    //lower temp every 10 iterations
    private double lowerTempModulo = 10;
    private int numberOfIterations = 100000;

    public double getTemperature() {
        return temperature;
    }

    public double getCoolingRate() {
        return coolingRate;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    //Holds all the best paths from an originId to each intersection of the graph
    private final Map<Long, Dijkstra> bestPaths;

    //our segments/intersections in form of a graph
    private final CityMapGraph cityMapGraph;

    //The steps the Traveling Salesman has to visit
    private final PlanningRequest planningRequest;

    private ArrayList<Pair<Long, String>> stepsIdentifiers;
    private ArrayList<Long> stepsIntersectionId;

    //used for the revert function
    private ArrayList<Pair<Long, String>> oldStepsIdentifiers;
    private ArrayList<Long> oldIntersectionIds;


    //
    public boolean mustStop = false;


    /**
     * Constructor of a SimulatedAnnealing object from a PlanningRequest and CityMapGraph
     *
     * @param planningRequest the planningRequest loaded into the application
     * @param cityMapGraph    the cityMapGraph loaded into the application
     */
    public SimulatedAnnealing(PlanningRequest planningRequest, CityMapGraph cityMapGraph) {
        this.planningRequest = planningRequest;
        this.cityMapGraph = cityMapGraph;
        this.bestPaths = new HashMap<>();
        this.stepsIdentifiers = new ArrayList<>();
        this.stepsIntersectionId = new ArrayList<>();
        computeAllShortestPaths();
    }

    /**
     * For every step the TSM has to visit, run Dijkstra from this intersectionId
     * and store the results in bestPaths
     * <p>
     * For every step, store in stepsIdentifiers the id of the request and its type
     * (pickup or delivery, or depot begin/end) , and store in the same index in stepsIntersectionId
     * the id of the associated intersection.
     * </p>
     */
    public void computeAllShortestPaths() {

        Dijkstra dijkstraData;
        Long depotId = planningRequest.getDepot().getIntersection().getId();

        //compute Dijkstra from the depot intersection
        dijkstraData = new Dijkstra(cityMapGraph, depotId);

        //store the result in our hashmap
        bestPaths.put(dijkstraData.getOriginId(), dijkstraData);

        //start the travel with the depot
        stepsIdentifiers.add(new Pair(-1L, "begin"));
        stepsIntersectionId.add(depotId);

        for (Request request : planningRequest.getRequests()) {

            Long requestId = request.getId();
            Pickup pickup = request.getPickup();
            Delivery delivery = request.getDelivery();
            //check if the Dijkstra has already been run from this intersection
            //to avoid re-computing for nothing...
            if (!bestPaths.containsKey(pickup.getIntersection().getId())) {
                dijkstraData = new Dijkstra(cityMapGraph, pickup.getIntersection().getId());
                bestPaths.put(pickup.getIntersection().getId(), dijkstraData);
            }

            stepsIdentifiers.add(new Pair(requestId, "pickup"));
            stepsIntersectionId.add(dijkstraData.getOriginId());


            dijkstraData = new Dijkstra(cityMapGraph, delivery.getIntersection().getId());
            bestPaths.put(dijkstraData.getOriginId(), dijkstraData);
            stepsIdentifiers.add(new Pair(requestId, "delivery"));
            stepsIntersectionId.add(dijkstraData.getOriginId());

        }

        //end the travel with the depot
        stepsIdentifiers.add(new Pair(-2L, "end"));
        stepsIntersectionId.add(depotId);


        //at this point, initialTravel = visitedSteps, and requests are delivered sequentially
    }

    /**
     * Inspired from <a href ="https://www.baeldung.com/java-simulated-annealing-for-traveling-salesman">this algorithm</a>
     * Simulated annealing algorithm to find an optimal path for our deliverer.
     * <p>
     * If the algorithm is running for longer than a defined MAXIMUM_TIME and timeoutEnabled is set to true, it interrupts itself and returns false.
     * Otherwise, it keeps running until done and returns true.
     * </p>
     * <p>
     * This implementation design allows us to interrupt the algorithm to ask the user if he wants the result now or if he wants to wait for a more optimized result.
     * If he wants to keep computing, because the state is saved in the class, when re-calling the function, the algorithm will continue from where it had stopped.
     * </p>
     * @param timeoutEnabled,   true if we're activating a timeout to ask the user to stop/continue
     * @param slowModeActivated true if the "slow mode" is activated for the demo
     * @return true if the algorithm has fully computed, false if the algorithm was interrupted
     */
    public boolean runSimulatedAnnealing(boolean timeoutEnabled, boolean slowModeActivated) {
        boolean swapResult;
        double bestDistance = getTotalDistance();
        double t = temperature;
        long start = System.currentTimeMillis();
        long timeElapsed = 0;

        if (slowModeActivated && timeoutEnabled) {
            this.numberOfIterations*=1000;
            this.finalTemperature/=100;
            this.lowerTempModulo=1000;
        }

        while (numberOfIterations > 0) {
            numberOfIterations--;
            timeElapsed = System.currentTimeMillis() - start;
            if (timeElapsed > MAXIMUM_TIME && timeoutEnabled) {
                return false;
            }
            //exit loop if t<finalTemperature
            if (t > finalTemperature) {
                //Store the old values to revert the swap if it's not suitable
                oldStepsIdentifiers = (ArrayList<Pair<Long, String>>) stepsIdentifiers.clone();
                oldIntersectionIds = (ArrayList<Long>) stepsIntersectionId.clone();
                do {
                    int stepsSize = stepsIntersectionId.size();
                    //Generates random number between zero and stepsSize-1
                    int swapFirstIndex = 1 + (int) (Math.random() * (stepsSize - 1));
                    int swapSecondIndex = 1 + (int) (Math.random() * (stepsSize - 1));
                    swapResult = swapSteps(swapFirstIndex, swapSecondIndex);

                    //If the swap is not allowed (can't have delivery X prior to pickup X, we retry to swap)
                } while (!swapResult);

                double currentDistance = getTotalDistance();
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                } else if ((Math.exp(bestDistance - currentDistance) / t) < Math.random()) {
                    revertSwapSteps(oldStepsIdentifiers, oldIntersectionIds);
                }
            } else if( !(slowModeActivated && timeoutEnabled)){
                break;
            }
            if(numberOfIterations%lowerTempModulo==0){
                t *= coolingRate;
                temperature = t;
            }


        }
        return true;
    }

    /**
     * @return total distance of the travel
     */
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
     * If the swap is allowed, stepsIdentifiers and stepsIntersectionId elements are swapped
     * in a new order
     *
     * @return false if the swap is not allowed / true if the swap is allowed
     */
    public boolean swapSteps(int swapFirstIndex, int swapSecondIndex) {


        ArrayList<Pair<Long, String>> cloneIdentifier = (ArrayList<Pair<Long, String>>) stepsIdentifiers.clone();
        int stepsSize = stepsIntersectionId.size();
        if (swapFirstIndex == 0 || swapFirstIndex == stepsSize - 1 || swapSecondIndex == 0 || swapSecondIndex == stepsSize - 1) {
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

    /**
     * reverts a swap using the old values of stepsIdentifiers and stepsIntersectionId
     *
     * @param oldStepsIdentifiers
     * @param oldIntersectionIds
     */
    public void revertSwapSteps(ArrayList<Pair<Long, String>> oldStepsIdentifiers, ArrayList<Long> oldIntersectionIds) {
        stepsIdentifiers = oldStepsIdentifiers;
        stepsIntersectionId = oldIntersectionIds;
    }

    /**
     * @return ArrayList of ordered steps Pairs(RequestId, typeOfRequest)
     */
    public ArrayList<Pair<Long, String>> getStepsIdentifiers() {
        return stepsIdentifiers;
    }

    /**
     * @return ArrayList of ordered steps intersection Ids
     */
    public ArrayList<Long> getStepsIntersectionId() {
        return stepsIntersectionId;
    }

    private Double getDistance() {
        return 0.0;
    }

    /**
     * @return Mapping of each originId with a Dijkstra computation result
     */
    public Map<Long, Dijkstra> getBestPaths() {
        return bestPaths;
    }

    /**
     * adds to this.bestPaths the result of Dijkstra computation from idOrigin
     *
     * @param idOrigin
     */
    public void addBestPath(long idOrigin) {
        Dijkstra dijkstraData;
        dijkstraData = new Dijkstra(cityMapGraph, idOrigin);
        bestPaths.put(dijkstraData.getOriginId(), dijkstraData);
    }


}
