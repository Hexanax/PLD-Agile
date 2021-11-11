package fr.insalyon.pldagile.model;

import javafx.util.Pair;

import java.util.*;

/**
 * Tour is model class that represents the list of ordered requests to visit computed from a planning request.
 * Tour is represented by a list of ordered requests,a depot, all visited intersections and
 * segments to build the way
 */
public class Tour {
    /**
     * speed of the deliverer in km/h
     */
    private final double SPEED_KMH = 15.0;

    /**
     * speed of the deliver in m/s
     */
    private final double SPEED_MS = SPEED_KMH / 3.6;

    //requests to perform
    private Map<Long, Request> requests;
    //ordered list of segments to follow to accomplish the tour
    private List<Segment> path;
    //ordered list of intersections to follow to accomplish the tour
    private List<Intersection> intersections;

    /**
     * stepsIdentifiers specify for each step of a tour his request id and is type of address
     * A step is a pickup, a depot, or a delivery
     */
    private ArrayList<Pair<Long, String>> stepsIdentifiers;
    private Depot depot;
    private double pickupsDuration;
    private double deliveriesDuration;
    private double travelsDuration;
    private double length;

    /**
     * Constructor of Tour.
     * @param requests the list of requests to perform
     * @param depot the depot
     */
    public Tour(List<Request> requests, Depot depot) {
        this.requests = new HashMap<>();
        for (Request request : requests) {
            this.requests.put(request.getId(), request);
        }
        this.path = new ArrayList<>();
        this.intersections = new ArrayList<>();
        this.depot = depot;
        this.pickupsDuration = 0;
        this.deliveriesDuration = 0;
        this.travelsDuration = 0;
        this.length = 0;

    }

    /**
     * Constructor of an empty Tour.
     */
    public Tour() {}

    /**
     * Copy constructor of Tour.
     * @param tour the tour you want to copy
     */
    public Tour(Tour tour) {
        this.requests = new HashMap<>(tour.requests);
        this.path = new ArrayList<>(tour.path);
        this.intersections = new ArrayList<>(tour.intersections);
        this.depot = tour.depot;
        this.pickupsDuration = tour.pickupsDuration;
        this.deliveriesDuration = tour.deliveriesDuration;
        this.travelsDuration = tour.travelsDuration;
        this.length = tour.length;
        this.stepsIdentifiers = new ArrayList<>(tour.stepsIdentifiers);

    }


    public Map<Long, Request> getRequests() {
        return requests;
    }

    public ArrayList<Pair<Long, String>> getSteps() {
        return stepsIdentifiers;
    }

    public List<Segment> getPath() {
        return path;
    }

    public Depot getDepot() {
        return depot;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public double getTravelsDuration() {
        return travelsDuration;
    }

    public double getPickupsDuration() {
        return pickupsDuration;
    }

    public double getDeliveriesDuration() {
        return deliveriesDuration;
    }

    public double getTourDuration() {
        return travelsDuration + pickupsDuration + deliveriesDuration;
    }

    public double getLength() {
        return length;
    }

    /**
     * Adds a single intersection at the end of the ordered list of intersections
     * @param intersection the intersection
     */
    public void addIntersection(Intersection intersection) {
        intersections.add(intersection);
    }

    /**
     * Adds a single segment at the end if the ordered list of segments
     * @param segment the segment
     */
    public void addSegment(Segment segment) {
        this.travelsDuration += segment.getLength() / SPEED_MS;
        this.length += segment.getLength();
        path.add(segment);
    }

    /**
     * Adds a single request in the requests list to perform
     * @param request the request
     */
    public void addRequest(Request request) {
        this.requests.put(request.getId(), request);
    }


    /**
     * Increments the pickup time of the tour
     * @param time the time to add
     */
    public void addPickupTime(double time) {
        this.pickupsDuration += time;
    }

    /**
     * Increments the delivery time of the tour
     * @param time the time to add
     */
    public void addDeliveryTime(double time) {
        this.deliveriesDuration += time;
    }

    /**
     * Increments the travel time of the tour
     * @param time the time to add
     */
    public void addTravelTime(double time) {
        this.travelsDuration += time;
    }

    public void setRequests(Map<Long, Request> requests) {
        this.requests = requests;
    }

    public void setPath(List<Segment> path) {
        this.path = path;
    }

    public void setDepotAddress(Depot depot) {
        this.depot = depot;
    }

    public void setStepsIdentifiers(ArrayList<Pair<Long, String>> stepsIdentifiers) {
        this.stepsIdentifiers = stepsIdentifiers;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = new ArrayList<Intersection>(intersections);
    }

    /**
     * Allow to re-initialize the different Tour durations
     */
    public void reset() {
        pickupsDuration = 0;
        deliveriesDuration = 0;
        travelsDuration = 0;
        this.path = new ArrayList<>();
    }

    /**
     * Adds a step to the tour after specify index in parameter
     * @param index the index
     * @param step the step
     */
    public void addStep(int index, Pair<Long, String> step) {
        ArrayList<Pair<Long, String>> newSteps = new ArrayList<>();

        int currentIndex = 0;
        for (Pair<Long, String> stepIdentify : this.stepsIdentifiers) {
            newSteps.add(stepIdentify);
            if (currentIndex == index) {
                newSteps.add(step);
            }
            currentIndex++;
        }

        this.stepsIdentifiers = newSteps;
    }

    /**
     * Removes the request of the tour and the corresponding steps that match with
     * the id request in parameter
     * @param idRequestDelete the id of the request to delete
     */
    public void deleteRequest(long idRequestDelete) {
        this.requests.remove(idRequestDelete);
        stepsIdentifiers.removeIf(nextElement -> nextElement.getKey() == idRequestDelete);
    }

}