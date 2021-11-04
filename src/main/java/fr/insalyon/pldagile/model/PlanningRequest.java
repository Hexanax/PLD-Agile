package fr.insalyon.pldagile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public void add(Depot depot) {
        this.depot = depot;
    }

    public void add(Request request) {
        Long unorderedId = (long) requests.size();
        request.setId(unorderedId);
        requests.add(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningRequest that = (PlanningRequest) o;
        return Objects.equals(requests, that.requests) && Objects.equals(depot, that.depot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requests, depot);
    }
}
