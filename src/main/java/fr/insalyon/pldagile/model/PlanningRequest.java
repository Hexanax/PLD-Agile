package fr.insalyon.pldagile.model;

import java.util.ArrayList;
import java.util.List;

public class PlanningRequest {
    private List<Request> requests;
    private Depot depot;

    public PlanningRequest(List<Request> requests, Depot depot) {
        this.requests = requests;
        this.depot = depot;
    }

    public PlanningRequest() {
        requests = new ArrayList<Request>();
    }

    public List<Request> getRequests() {

        return requests;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public void setDepotAddress(Depot depot) {
        this.depot = depot;
    }

    public void add(Depot depot) {
        this.depot = depot;
    }

    public void add(Request request){
        Long unorderedId = (long) requests.size();
        request.setId(unorderedId);
        requests.add(request);
    }
}
