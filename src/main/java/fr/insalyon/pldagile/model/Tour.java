package fr.insalyon.pldagile.model;

import java.util.List;

public class Tour {
    // An ordered list of requests
    private List<Request> requests;
    private List<Segment> path;
    private Depot depot;

    public Tour(List<Request> requests, List<Segment> path, Depot depot) {
        this.requests = requests;
        this.path = path;
        this.depot = depot;
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
