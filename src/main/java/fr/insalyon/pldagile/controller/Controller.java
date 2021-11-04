package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLCityMap;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

import static javafx.application.Application.launch;

public class Controller {
    private PCLCityMap pclCityMap;
    private PCLPlanningRequest pclPlanningRequest;
    private Tour tour;
    private Tour modifyTour;
    private State currentState;
    private Window window;
    private ListOfCommands listOfCommands;


    protected Pair<Integer, Pickup> pickupToAdd;
    protected Pair<Integer, Delivery> deliveryToAdd;

    protected final InitialState initialState = new InitialState();
    protected final MapDisplayedState mapDisplayedState = new MapDisplayedState();
    protected final MapOverwrite1State mapOverwrite1State = new MapOverwrite1State();
    protected final MapOverwrite2State mapOverwrite2State = new MapOverwrite2State();
    protected final MapOverwrite3State mapOverwrite3State = new MapOverwrite3State();
    protected final RequestsDisplayedState requestsDisplayedState = new RequestsDisplayedState();
    protected final RequestsOverwrite1State requestsOverwrite1State = new RequestsOverwrite1State();
    protected final RequestsOverwrite2State requestsOverwrite2State = new RequestsOverwrite2State();
    protected final TourComputedState tourComputedState = new TourComputedState();
    protected final DeleteRequestState1 deleteRequestState1 = new DeleteRequestState1();
    protected final AddRequestState1 addRequestState1 = new AddRequestState1();
    protected final AddRequestState2 addRequestState2 = new AddRequestState2();
    protected final AddRequestState3 addRequestState3 = new AddRequestState3();
    protected final AddRequestState4 addRequestState4 = new AddRequestState4();
    protected final AddRequestState5 addRequestState5 = new AddRequestState5();
    protected final AddRequestState6 addRequestState6 = new AddRequestState6();


    public Controller(CityMap citymap, PlanningRequest planningRequest, Tour tour) {
        this.pclCityMap = new PCLCityMap(citymap);
        this.pclPlanningRequest = new PCLPlanningRequest(planningRequest);
        this.tour = tour;
        listOfCommands = new ListOfCommands();
        currentState = initialState;
    }

    public PCLCityMap getPclCityMap() {
        return pclCityMap;
    }

    public void initWindow(Window window) {
        this.window = window;
    }

    protected void setCurrentState(State state) {
        currentState = state;
    }

    protected void setCityMap(CityMap cityMap) {
        pclCityMap.setCityMap(cityMap);
    }

    public PCLPlanningRequest getPclPlanningRequest() {
        return pclPlanningRequest;
    }

    protected void setPlanningRequest(PlanningRequest planningRequest) {
        this.pclPlanningRequest.setPlanningRequest(planningRequest);
    }

    protected void setTour(Tour tour) {
        this.tour = tour;
    }

    protected void initializeModifyTour() {
        modifyTour = new Tour(tour);
    }

    protected void validModifyTour() {
        tour = modifyTour;
    }

    protected void setModifyTour(Tour tour) {
        this.modifyTour = tour;
    }

    /**
     * Method called by window after a click on the button "Load a plan"
     */
    public void loadMap() {
        currentState.loadMap(this, window);
    }

    public void loadRequests() {
        currentState.loadRequests(this, pclCityMap.getCityMap(), window);
    }

    public void computeTour() {
        currentState.computeTour(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), window);
    }

    public void cancel() {
        currentState.cancel(this, tour, window, listOfCommands);
    }

    public void confirm(String result) {
        currentState.confirm(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), tour, result, window, listOfCommands);
    }


    public void generateRoadMap() {
        currentState.generateRoadMap(this, tour, window);
    }

    public void deleteRequest(Long idRequest) {
        currentState.deleteRequest(this, pclCityMap.getCityMap(), tour, tour.getRequests().get(idRequest), window, listOfCommands);
    }

    public void modifyClick(Long id, String type, int stepIndex) {
        currentState.modifyClick(this, id, type, stepIndex, window);
    }

    public void addRequest(Long id) {
        currentState.addRequest(this, pclCityMap.getCityMap(), tour, id, window);
    }

    /**
     * Method called by window after a click on the button "Undo"
     */
    public void undo() {
        currentState.undo(listOfCommands, window, tour);
    }

    /**
     * Method called by window after a click on the button "Redo"
     */
    public void redo() {
        currentState.redo(listOfCommands, window, tour);
    }
}
