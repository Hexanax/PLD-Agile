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
    protected final MapOverwrite1State mapOverwrite1State = new MapOverwrite1State();
    protected final MapOverwrite2State mapOverwrite2State = new MapOverwrite2State();
    protected final MapOverwrite3State mapOverwrite3State = new MapOverwrite3State();
    protected final RequestsDisplayedState requestsDisplayedState = new RequestsDisplayedState();
    protected final RequestsOverwrite1State requestsOverwrite1State = new RequestsOverwrite1State();
    protected final RequestsOverwrite2State requestsOverwrite2State = new RequestsOverwrite2State();
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
    protected void setCitymap(CityMap citymap) {this.citymap = citymap; }
    protected void setPlanningRequest(PlanningRequest planningRequest) {this.planningRequest = planningRequest;}
    protected void setTour(Tour tour){this.tour = tour;}
    /**
     * Method called by window after a click on the button "Load a plan"
     */
    public void loadMap() {
        currentState.loadMap(this, citymap, window);
    }

    public void loadRequests() { currentState.loadRequests(this,citymap, planningRequest,window);}

    public void computeTour() { currentState.computeTour(this,citymap, planningRequest,tour, window);}

    public void cancel() { currentState.cancel(this);}

    public void confirm() { currentState.confirm(this,citymap,planningRequest,window);}
}
