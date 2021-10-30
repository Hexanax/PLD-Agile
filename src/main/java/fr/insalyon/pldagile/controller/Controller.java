package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

import static javafx.application.Application.launch;

public class Controller {
    private CityMap citymap;
    private PlanningRequest planningRequest;
    private Tour tour;
    private Tour modifyTour;
    private State currentState;
    private Window window;

    protected Request requestToDelete;

    protected final InitialState initialState = new InitialState();
    protected final MapDisplayedState mapDisplayedState = new MapDisplayedState();
    protected final MapOverwrite1State mapOverwrite1State = new MapOverwrite1State();
    protected final MapOverwrite2State mapOverwrite2State = new MapOverwrite2State();
    protected final MapOverwrite3State mapOverwrite3State = new MapOverwrite3State();
    protected final RequestsDisplayedState requestsDisplayedState = new RequestsDisplayedState();
    protected final RequestsOverwrite1State requestsOverwrite1State = new RequestsOverwrite1State();
    protected final RequestsOverwrite2State requestsOverwrite2State = new RequestsOverwrite2State();
    protected final TourComputedState tourComputedState = new TourComputedState();
    protected final ModifyTourState modifyTourState = new ModifyTourState();
    protected final DeleteRequestState1 deleteRequestState1 = new DeleteRequestState1();
    protected final DeleteRequestState2 deleteRequestState2 = new DeleteRequestState2();

    public Controller(CityMap citymap, PlanningRequest planningRequest, Tour tour) {
        this.citymap = citymap;
        this.planningRequest = planningRequest;
        this.tour = tour;
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
    protected void initializeModifyTour() {
        modifyTour = new Tour(tour);
    }
    protected void validModifyTour() {
        tour = modifyTour;
    }
    protected void setModifyTour(Tour tour){this.modifyTour = tour;}

    /**
     * Method called by window after a click on the button "Load a plan"
     */
    public void loadMap() {
        currentState.loadMap(this, citymap, window);
    }

    public void loadRequests() { currentState.loadRequests(this,citymap, planningRequest,window);}

    public void computeTour() { currentState.computeTour(this,citymap, planningRequest, window);}

    public void cancel() { currentState.cancel(this, tour,modifyTour,window);}

    public void confirm() { currentState.confirm(this,citymap,planningRequest,tour, modifyTour,window);}

    public void modify() { currentState.modify(this,window);}

    public void generateRoadMap() { currentState.generateRoadMap(this,tour,window);}

    public void deleteRequest(Long idRequest) { currentState.deleteRequest(this,citymap, tour,modifyTour, modifyTour.getRequests().get(idRequest), window);}



}
