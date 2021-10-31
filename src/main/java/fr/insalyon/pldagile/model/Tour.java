package fr.insalyon.pldagile.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tour {
    private final double SPEED_KMH = 15.0;
    private final double SPEED_MS = 15.0/3.6;

    private Long nextRequestId;

    // An ordered list of requests
    private Map<Long,Request> requests;
    private List<Segment> path;
    private List<Intersection> intersections;
    private ArrayList<Pair<Long, String>> stepsIdentifiers;
    private Depot depot;
    private double pickupsDuration;
    private double deliveriesDuration;
    private double travelsDuration;
    private double length;

    public Tour(List<Request> requests, Depot depot) {
        this.requests = new HashMap<>();
        for(Request request : requests) {
            this.requests.put(request.getId(),request);
        }
        this.nextRequestId = Long.parseLong(String.valueOf(this.requests.size()));
        this.path = new ArrayList<>();
        this.intersections = new ArrayList<>();
        this.depot = depot;
        this.pickupsDuration = 0;
        this.deliveriesDuration = 0;
        this.travelsDuration = 0;
        this.length = 0;

    }

    public Tour() {

    }

    public Tour(Tour tour) {
        this.requests = new HashMap<>(tour.requests);
        this.path = new ArrayList<>(tour.path);
        this.intersections = new ArrayList<>(tour.intersections);
        this.depot = tour.depot;
        this.pickupsDuration = tour.pickupsDuration;
        this.deliveriesDuration = tour.deliveriesDuration;
        this.travelsDuration = tour.travelsDuration;
        this.length = tour.length;
        this.stepsIdentifiers = new ArrayList<Pair<Long, String>>(tour.stepsIdentifiers);
        this.nextRequestId = tour.nextRequestId;
    }

    public Map<Long,Request> getRequests() {
        return requests;
    }

    public ArrayList<Pair<Long,String>> getSteps(){return stepsIdentifiers;}
    public List<Segment> getPath() {
        return path;
    }

    public Depot getDepot() {
        return depot;
    }

    public List<Intersection> getIntersections() {return intersections;}

    public double getTravelsDuration() { return travelsDuration;}

    public double getPickupsDuration() {return pickupsDuration;}

    public double getDeliveriesDuration(){return deliveriesDuration;}

    public double getTourDuration() {return travelsDuration+pickupsDuration+deliveriesDuration;}

    public double getLength() {return length;}

    public void addIntersection(Intersection intersection){
        intersections.add(intersection);
    }

    public void addSegment(Segment segment) {
        this.travelsDuration += segment.getLength()/SPEED_MS;
        this.length += segment.getLength();
        path.add(segment);
    }

    public void addRequest(Request request){
        this.requests.put(request.getId(), request);
        nextRequestId++;
    }
    public long getNextRequestId() {return nextRequestId;}

    public void addPickupTime(double time) {
        this.pickupsDuration += time;
    }

    public void addDeliveryTime(double time) {
        this.deliveriesDuration += time;
    }

    public void addTravelTime(double time) {
        this.travelsDuration += time;
    }

    public void setRequests(Map<Long,Request> requests) {
        this.requests = requests;
    }

    public void setPath(List<Segment> path) {
        this.path = path;
    }

    public void setDepotAddress(Depot depot) {
        this.depot = depot;
    }

    public void setStepsIdentifiers(ArrayList<Pair<Long,String>> stepsIdentifiers) {this.stepsIdentifiers = stepsIdentifiers;}

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = new ArrayList<Intersection>(intersections);}

    public void reset(){
        pickupsDuration =0;
        deliveriesDuration =0;
        travelsDuration =0;
        this.path = new ArrayList<>();
    }

    public void setNextRequestId(long l) {
        this.nextRequestId = l-1;
    }
}
