package fr.insalyon.pldagile.model;

import java.util.List;

public class Tour {
    // An ordered list of requests
    private List<Request> requests;
    private List<Segment> path;
    private DepotAddress depotAddress;

    public Tour(List<Request> requests, List<Segment> path, DepotAddress depotAddress) {
        this.requests = requests;
        this.path = path;
        this.depotAddress = depotAddress;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Segment> getPath() {
        return path;
    }

    public DepotAddress getDepotAddress() {
        return depotAddress;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public void setPath(List<Segment> path) {
        this.path = path;
    }

    public void setDepotAddress(DepotAddress depotAddress) {
        this.depotAddress = depotAddress;
    }
}
