package fr.insalyon.pldagile.model;

import java.util.List;

public class PlanningRequest {
    private long id;
    private List<Request> requests;
    private DepotAddress depotAddress;

    public PlanningRequest(List<Request> requests, DepotAddress depotAddress) {
        this.requests = requests;
        this.depotAddress = depotAddress;
    }

    public List<Request> getRequests() {

        return requests;
    }

    public DepotAddress getDepotAddress() {
        return depotAddress;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public void setDepotAddress(DepotAddress depotAddress) {
        this.depotAddress = depotAddress;
    }
}
