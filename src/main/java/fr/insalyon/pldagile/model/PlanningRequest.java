package fr.insalyon.pldagile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanningRequest {

    private long currentID;
    private List<Request> requests;
    private Depot depot;

    public PlanningRequest(List<Request> requests, Depot depot) {
        this.requests = requests;
        this.depot = depot;
        this.currentID = requests.size();
    }

    public PlanningRequest(PlanningRequest planningRequest){
        this.requests = planningRequest.requests;
        this.depot = planningRequest.depot;
        this.currentID = planningRequest.currentID;
    }

    public PlanningRequest() {
        requests = new ArrayList<Request>();
        this.currentID = 0;
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
        add(request, true);
    }

    public void add(Request request, boolean assignId) {
        if(assignId) {
            request.setId(currentID);
            currentID++;
        }
        requests.add(request);
    }

    public void deleteLastRequest(){
        int index = requests.size()-1;
        requests.remove(index);
    }

    public void deleteRequest(long idRequest){
        requests.removeIf(value -> value.getId() == idRequest);
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

    public Request getLastRequest() {
        return requests.get(requests.size()-1);
    }

}
