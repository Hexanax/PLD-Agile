package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.*;
import javafx.util.Pair;

import java.util.*;

/**
 * This class allows all the algorithmic treatments to be carried out on the tour.
 * It allows to calculate the shortest path to realize it.
 * It allows to calculate the exact time of arrival at each address and the global time of the tour.
 * It also allows to add and remove requests to the tour by recalculating locally the changes.
 */
public class TourBuilderV2 {

    private static SimulatedAnnealing simulatedAnnealing;

    public static SimulatedAnnealing getSimulatedAnnealing() {
        return simulatedAnnealing;
    }

    public Tour buildTour(PlanningRequest planningRequest, CityMap cityMap, boolean slowModeActivated, Runnable notFullyComputed) throws ExceptionCityMap {

        //List of ordered intersections to visit during the tour
        List<Long> tourIntersections = new ArrayList<>();
        CityMapGraph cityMapGraph = new CityMapGraph(cityMap);

        boolean isFullyComputed = false;
        //SimulatedAnnealing runs on the planningRequest applied to the graph to find an optimized tour
        simulatedAnnealing = new SimulatedAnnealing(planningRequest, cityMapGraph);
        isFullyComputed = simulatedAnnealing.runSimulatedAnnealing(true, slowModeActivated);
        if (!isFullyComputed) {
            notFullyComputed.run();
        }

        Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();

        //ArrayList of ordered **STEPS** to visit, computed by the simulatedAnnealing algorithm.
        ArrayList<Long> intersectionSteps = new ArrayList<>(simulatedAnnealing.getStepsIntersectionId());

        //Iterate over all intersectionSteps to compute the full tour, with intermediary intersections
        //in the _tourIntersections_ List

        Long previousIntersection = intersectionSteps.get(0);
        for (Long destinationId : intersectionSteps.subList(1, intersectionSteps.size())) {

            Dijkstra bestPathsFromOrigin = bestPaths.get(previousIntersection);
            List<Long> localTravel = bestPathsFromOrigin.getShortestPath(destinationId);

            //Don't add the last intersection to the tourIntersections, because it will
            //Be added as the first intersection of the next travel
            tourIntersections.addAll(localTravel.subList(0, localTravel.size() - 1));
            previousIntersection = destinationId;
        }
        //We have to manually add the depot intersection to the end of the list
        tourIntersections.add(intersectionSteps.get(0));

        Tour tour = new Tour(planningRequest.getRequests(), planningRequest.getDepot());
        Map<Long, Intersection> intersections = cityMap.getIntersections();


        for (long idIntersection : tourIntersections) {
            tour.addIntersection(intersections.get(idIntersection));
        }

        tour.setStepsIdentifiers(simulatedAnnealing.getStepsIdentifiers());
        return computeTour(cityMap, tour, tour.getIntersections());
    }

    /**
     * Allows to delete the request in parameter from the current computed tour.
     * We tour will be recomputed locally by performing Dijskra between the address
     * that came before the pickup and the address that came after the pickup in the steps.
     * Same for the delivery.
     * @param cityMap the citymap
     * @param tour the tour
     * @param request the request to delete
     * @return the new tour without the deleted request
     * @throws ExceptionCityMap throws if the application detect a dead end intersection
     */
    public Tour deleteRequest(CityMap cityMap, Tour tour, Request request) throws ExceptionCityMap {
        Map<Long, Request> requests = tour.getRequests();
        Depot depot = tour.getDepot();
        Map<Long, Intersection> intersectionsMap = cityMap.getIntersections();

        /**
         * 0 -> index of the address previous the pickup to delete in the steps
         * 1 -> index of the address next the pickup to delete in the steps
         * 2 -> index of the address previous the delivery to delete in the steps
         * 3 -> index of the address next the delivery to delete in the steps
         */
        int[] indexAroundStep = new int[4];

        List<Intersection> intersections = tour.getIntersections();
        List<Intersection> newIntersections = new ArrayList<>();
        List<Pair<Long, String>> steps = new ArrayList<Pair<Long, String>>(tour.getSteps());
        steps.remove(0);

        boolean found = false;
        boolean pickup = false;
        boolean delivery = false;
        int lastFoundIndex = 0;
        int index = 1;

        /**
         * iterate over intersections to find the 2 intersections with the same IDs as the ones
         * in the request we want to delete.
         * in indexAroundStep we store the index of the intersections before/after the pickup/delivery
         */
        long nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(0));
        while (!found) {
            if (intersections.get(index).getId() == nextSpecificIntersection) {
                if (Objects.equals(steps.get(0).getKey(), request.getId())) {
                    if (steps.get(0).getValue() == "pickup") {
                        indexAroundStep[0] = lastFoundIndex;
                        pickup = true;
                    } else {
                        indexAroundStep[2] = lastFoundIndex;
                        delivery = true;
                    }
                } else {
                    lastFoundIndex = index;
                    if (pickup) {
                        pickup = false;
                        indexAroundStep[1] = lastFoundIndex;
                    }
                    if (delivery) {
                        delivery = false;
                        indexAroundStep[3] = lastFoundIndex;
                        found = true;
                    }
                }
                steps.remove(0);
                if (!found) {
                    nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(0));
                }

            }
            index++;

        }

        //Remove the steps linked to the deleted request from the tour
        tour.getSteps().removeIf(step -> Objects.equals(step.getKey(), request.getId()));

        index = 0;
        while (index != indexAroundStep[0]) {
            newIntersections.add(intersections.get(index));
            index++;
        }

        /**
         * Recomputes locally the tour
         * There are 3 possible cases :
         * First the pickup is just befor the delivery to delete
         * Second there is only one adress between the pickup and the delivery
         * Third all other cases
         */
        if (indexAroundStep[0] == indexAroundStep[2]) {
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for (long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())) {
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

            //if intersection after pickup is the same as before delivery
        } else if (indexAroundStep[1] == indexAroundStep[2]) {
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for (long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())) {
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

            newIntersections.remove(newIntersections.size() - 1);


            dijkstra = bestPaths.get(intersections.get(indexAroundStep[2]).getId());
            for (long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[3]).getId())) {
                newIntersections.add(intersectionsMap.get(idIntersection));
            }
        } else {
            Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
            Dijkstra dijkstra = bestPaths.get(intersections.get(indexAroundStep[0]).getId());

            for (long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[1]).getId())) {
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

            index = indexAroundStep[1] + 1;
            while (index != indexAroundStep[2]) {
                newIntersections.add(intersections.get(index));
                index++;
            }

            dijkstra = bestPaths.get(intersections.get(indexAroundStep[2]).getId());
            for (long idIntersection : dijkstra.getShortestPath(intersections.get(indexAroundStep[3]).getId())) {
                newIntersections.add(intersectionsMap.get(idIntersection));
            }

        }

        index = indexAroundStep[3] + 1;
        while (index != intersections.size()) {
            newIntersections.add(intersections.get(index));
            index++;
        }

        tour.setIntersections(newIntersections);

        //resets the tour duration
        tour.reset();
        return new Tour(computeTour(cityMap, tour, newIntersections));
    }


    /**
     * Allows to add the request with the id in parameter from the current computed tour.
     * The tour will be recomputed locally by performing Dijskra between the address
     * that came before the pickup and the address that came after the pickup in the steps.
     * Same for the delivery.
     * @param cityMap the city map
     * @param tour the tour
     * @param planningRequestId the id of the request that we want to add and that is currentlyin the planning request
     * @return the new tour without the added request
     * @throws ExceptionCityMap throws if the application detect a dead end intersection
     */
    public Tour addRequest(CityMap cityMap, Tour tour, long planningRequestId) throws ExceptionCityMap {

        //Rebuild the tour
        Map<Long, Intersection> intersectionsMap = cityMap.getIntersections();
        Depot depot = tour.getDepot();
        List<Intersection> intersections = tour.getIntersections();
        List<Intersection> newIntersections = new ArrayList<>();
        ArrayList<Pair<Long, String>> steps = tour.getSteps();

        Map<Long, Request> requests = tour.getRequests();
        Request request = requests.get(planningRequestId);
        Pickup pickup = request.getPickup();
        Delivery delivery = request.getDelivery();

        //The shortest paths must be recalculated for the two new addresses present in the tour
        simulatedAnnealing.addBestPath(pickup.getIntersection().getId());
        simulatedAnnealing.addBestPath(delivery.getIntersection().getId());
        Map<Long, Dijkstra> bestPaths = simulatedAnnealing.getBestPaths();
        simulatedAnnealing.runSimulatedAnnealing(false,false);

        int indexStep = 0;
        int indexIntersection = 0;
        boolean add = true;
        boolean complete = false;
        boolean pickupDone = false;

        /**
         * We go around to find the steps we want to remove
         * We add accordingly the intersections that we do not seek to modify
         * If we fall on one of the two addresses that we want to delete,
         * then we recalculate locally the tour by fanning dijskra between the address
         * before and the one after and we remove the old intersections between these 2 points.
         */
        for (Pair<Long, String> step : tour.getSteps()) {

            /**
             * If we find the pickup there can be 2 possibilities
             * The first is if the delivery to be deleted is the step that comes after the pickup.
             * In this case you have to go and find the step after to recompute.
             * The second one is the other cases
             */
            if (Objects.equals(step.getKey(), request.getId()) && !pickupDone) {
                pickupDone = true;
                long beforePickupAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep - 1));
                long afterPickupAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep + 1));

                Dijkstra dijkstra = bestPaths.get(beforePickupAction);

                for (long idIntersection : dijkstra.getShortestPath(pickup.getIntersection().getId())) {
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size() - 1);

                long idIntersectionRelay = pickup.getIntersection().getId();
                if (Objects.equals(steps.get(indexStep + 1).getKey(), request.getId())) {
                    //complete because we have already deleted pickup and delivery
                    complete = true;
                    dijkstra = bestPaths.get(idIntersectionRelay);
                    for (long idIntersection : dijkstra.getShortestPath(delivery.getIntersection().getId())) {
                        newIntersections.add(intersectionsMap.get(idIntersection));
                    }
                    newIntersections.remove(newIntersections.size() - 1);
                    idIntersectionRelay = delivery.getIntersection().getId();
                    afterPickupAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep + 2));
                }

                dijkstra = bestPaths.get(idIntersectionRelay);
                for (long idIntersection : dijkstra.getShortestPath(afterPickupAction)) {
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size() - 1);
                add = false;
            } else if (Objects.equals(step.getKey(), request.getId()) && !complete) {
                long beforeDeliveryAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep - 1));
                long afterDeliveryAction = getValueOfNextIntersection(depot, requests, steps.get(indexStep + 1));

                Dijkstra dijkstra = bestPaths.get(beforeDeliveryAction);
                for (long idIntersection : dijkstra.getShortestPath(delivery.getIntersection().getId())) {
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size() - 1);
                dijkstra = bestPaths.get(delivery.getIntersection().getId());

                for (long idIntersection : dijkstra.getShortestPath(afterDeliveryAction)) {
                    newIntersections.add(intersectionsMap.get(idIntersection));
                }
                newIntersections.remove(newIntersections.size() - 1);
                add = false;
            } else if (!Objects.equals(step.getKey(), request.getId())) {
                long nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(indexStep));
                while (intersections.get(indexIntersection).getId() != nextSpecificIntersection && !Objects.equals(step.getKey(), request.getId())) {

                    //It is added that in the case where the intersections are not part of the old road that has been removed
                    if (add) {
                        newIntersections.add(intersections.get(indexIntersection));
                    }
                    indexIntersection++;
                }
                add = true;
            }
            indexStep++;
        }

        if (newIntersections.get(newIntersections.size() - 1).getId() != depot.getIntersection().getId()) {
            newIntersections.add(depot.getIntersection());
        }

        tour.setIntersections(newIntersections);
        tour.reset();

        return new Tour(computeTour(cityMap, tour, newIntersections));
    }

    /**
     * Allows you to retrieve the id of the intersection
     * where the next step of the turn is located
     * @param depot the depot
     * @param requests the list of requests
     * @param step the current step
     * @return the intersection id of the next step
     */
    private long getValueOfNextIntersection(Depot depot, Map<Long, Request> requests, Pair<Long, String> step) {
        if (Objects.equals(step.getValue(), "pickup")) {
            return requests.get(step.getKey()).getPickup().getIntersection().getId();
        }
        if (Objects.equals(step.getValue(), "delivery")) {
            return requests.get(step.getKey()).getDelivery().getIntersection().getId();
        }
        return depot.getIntersection().getId();
    }

    /**
     * Allows you to identify the segments that will be covered during
     * the tour and therefore to determine the length of the tour, its pickup, delivery and travel time.
     * @param cityMap the city map
     * @param tour the tour
     * @param intersections the list of ordered intersections of the tour
     * @return the tour fully calculated
     * @throws ExceptionCityMap throw if there is a dead end intersection
     */
    public Tour computeTour(CityMap cityMap, Tour tour, List<Intersection> intersections) throws ExceptionCityMap {
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();
        Depot depot = tour.getDepot();
        Map<Long, Request> requests = tour.getRequests();
        ArrayList<Pair<Long, String>> steps = tour.getSteps();
        List<Intersection> copyIntersections = new ArrayList<>(intersections);

        //If we calculate the tour with a dead end the algorithm can give a result but not with the right start or the right finish
        if (intersections.get(0).getId() != intersections.get(intersections.size() - 1).getId() || intersections.get(0).getId() != depot.getIntersection().getId()) {
            throw new ExceptionCityMap("An address of a request is unreachable with the current loaded city map");
        }

        int stepIndex = 1;
        long nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(stepIndex));
        long previous = copyIntersections.get(0).getId();
        copyIntersections.remove(0);
        for (Intersection intersection : copyIntersections) {
            long current = intersection.getId();
            Segment currentSegment = segments.get(new Pair<>(previous, current));
            //If we calculate the tour with a dead end the algorithm can give a null segment
            if (currentSegment == null) {
                throw new ExceptionCityMap("Segment is unreachable, an address of a request is unreachable with the current loaded city map");
            }


            tour.addSegment(currentSegment);
            if (current == nextSpecificIntersection) {
                double tourDuration = tour.getTourDuration() * 1000;
                Pair<Long, String> step = steps.get(stepIndex);
                if (Objects.equals(step.getValue(), "pickup")) {
                    Pickup pickup = requests.get(step.getKey()).getPickup();
                    tour.addPickupTime(pickup.getDuration());
                    pickup.setArrivalTime((int) (depot.getDepartureTime().getTime() + tourDuration));
                    stepIndex++;
                }
                if (Objects.equals(step.getValue(), "delivery")) {
                    Delivery delivery = requests.get(step.getKey()).getDelivery();
                    tour.addDeliveryTime(delivery.getDuration());
                    delivery.setArrivalTime((int) (depot.getDepartureTime().getTime() + tourDuration));
                    stepIndex++;
                }
                nextSpecificIntersection = getValueOfNextIntersection(depot, requests, steps.get(stepIndex));
            }

            previous = current;
        }

        return tour;
    }

    /**
     * Allows to test if an intersection is a dead end intersection
     * An intersection is a dead end if you can enter but not leave or vice versa
     * @param cityMap the city map
     * @param idIntersection the id of the intersection
     * @return false if it is not a deadEndIntersection
     */
    public boolean deadEndIntersection(CityMap cityMap, Long idIntersection) {
        boolean result = true;
        Map<Pair<Long, Long>, Segment> segments = cityMap.getSegments();

        boolean origin = false;
        boolean destination = false;
        for (Pair<Long, Long> intersections : segments.keySet()) {
            //Segment that go in
            if (Objects.equals(intersections.getKey(), idIntersection)) {
                origin = true;
            }
            //Segment that go out
            if (Objects.equals(intersections.getValue(), idIntersection)) {
                destination = true;
            }
        }

        if (origin && destination) {
            result = false;
        }

        return result;
    }


}