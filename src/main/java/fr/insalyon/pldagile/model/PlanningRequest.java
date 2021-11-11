package fr.insalyon.pldagile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PlanningRequest is model class that represents the list of unordered requests we load from xml files into the application.
 * PlanningRequest is represented by a list of unordered requests and a depot
 */
public class PlanningRequest {

    private long currentID;
    private List<Request> requests;
    private Depot depot;

    /**
     * Constructor of PlanningRequest.
     * @param requests the list of requests
     * @param depot the depot
     */
    public PlanningRequest(List<Request> requests, Depot depot) {
        this.requests = requests;
        this.depot = depot;
        this.currentID = requests.size();
    }

    /**
     * Copy constructor of PlanningRequest.
     * @param planningRequest the planning request you want to copy
     */
    public PlanningRequest(PlanningRequest planningRequest){
        this.requests = planningRequest.requests;
        this.depot = planningRequest.depot;
        this.currentID = planningRequest.currentID;
    }

    /**
     * Constructor of an empty PlanningRequest.
     */
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

    /**
     * Adds a depot to the PlanningRequest
     * @param depot the depot
     */
    public void add(Depot depot) {
        this.depot = depot;
    }

    /**
     * Adds an existing request to the PlanningRequest
     * @param request the request
     */
    public void add(Request request) {
        add(request, true);
    }

    /**
     * Adds a single new request and assigns it an id or not
     * @param request the request
     * @param assignId if true assigns an id to the request
     */
    public void add(Request request, boolean assignId) {
        if(assignId) {
            request.setId(currentID);
            currentID++;
        }
        requests.add(request);
    }

    /**
     * Deletes the last request insert in the list of requests
     */
    public void deleteLastRequest(){
        int index = requests.size()-1;
        requests.remove(index);
    }

    /**
     * Deletes requests in the list with the id in parameter
     * @param idRequest the id of requests to delete
     */
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

    /**
     * Get the last request in the list of the planning request
     * @return the last request added
     */
    public Request getLastRequest() {
        return requests.get(requests.size()-1);
    }

}
