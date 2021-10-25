package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

import static javafx.application.Application.launch;

public class Controller {
    private CityMap citymap;
    private PlanningRequest planningRequest;
    private Tour tour;
    private State currentState;
    private Window window;

    protected final InitialState initialState = new InitialState();
    protected final MapDisplayedState mapDisplayedState = new MapDisplayedState();
    protected final RequestsDisplayedState requestsDisplayedState = new RequestsDisplayedState();
    protected final TourComputedState tourComputedState = new TourComputedState();

    public Controller(CityMap citymap, PlanningRequest planningRequest) {
        this.citymap = citymap;
        this.planningRequest = planningRequest;
        this.tour = null;
        currentState = initialState;

    }

    public void initWindow(Window window) {
        this.window = window;
    }

    protected void setCurrentState(State state){
        currentState = state;
    }

    /**
     * Method called by window after a click on the button "Load a plan"
     */
    public void loadMap() {
        currentState.loadMap(this, citymap, window);
    }

    public void loadRequests() { currentState.loadRequests(this,citymap, planningRequest,window);}

    public void computeTour() { currentState.computeTour(this,citymap, planningRequest,tour, window);}
}
