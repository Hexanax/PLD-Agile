package fr.insalyon.pldagile.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tour {
    private final double SPEED_KMH = 15.0;
    private final double SPEED_MS = 15.0/3.6;

    // An ordered list of requests
    private List<Request> requests;
    private List<Segment> path;
    private List<Intersection> intersections;
    private Depot depot;
    private int pickupsDuration;
    private int deliveriesDuration;
    private double travelsDuration;

    public Tour(List<Request> requests, Depot depot) {
        this.requests = requests;
        this.path = new ArrayList<>();
        this.intersections = new ArrayList<>();
        this.depot = depot;
        this.pickupsDuration = 0;
        this.deliveriesDuration = 0;
        this.travelsDuration = 0;

    }

    public Tour() {

    }

    public List<Request> getRequests() {
        return requests;
    }


    public List<Segment> getPath() {
        return path;
    }

    public Depot getDepot() {
        return depot;
    }

    public List<Intersection> getIntersections() {return intersections;}



    public void addIntersection(Intersection intersection){
        intersections.add(intersection);
    }

    public void addSegment(Segment segment) {
        this.travelsDuration += segment.getLength()/SPEED_MS;
        path.add(segment);
    }

    public void addPickupTime(int time) {
        this.pickupsDuration += time;
    }

    public void addDeliveryTime(int time) {
        this.deliveriesDuration += time;
    }

    public void addTravelTime(int time) {
        this.travelsDuration += time;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public void setPath(List<Segment> path) {
        this.path = path;
    }

    public void setDepotAddress(Depot depot) {
        this.depot = depot;
    }


}
