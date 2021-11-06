package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLCityMap;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.CityMapView;
import fr.insalyon.pldagile.view.Window;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import static javafx.application.Application.launch;

public class Controller {

    //The controller and application's state
    private State currentState;
    private final PCLCityMap pclCityMap;
    private final PCLPlanningRequest pclPlanningRequest;
    private final PCLTour pclTour;
    private final Window window;
    private final ListOfCommands listOfCommands;

    protected Pair<Integer, Pickup> pickupToAdd;
    protected Pair<Integer, Delivery> deliveryToAdd;

    // Instances associated with each possible state of the controller
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

    public Controller() {
        this.pclCityMap = new PCLCityMap();
        this.pclPlanningRequest = new PCLPlanningRequest();
        this.pclTour = new PCLTour();
        this.listOfCommands = new ListOfCommands();
        this.currentState = initialState;
        this.window = new Window(this);
    }

    public PCLCityMap getPclCityMap() {
        return pclCityMap;
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

    public PCLTour getPclTour() {
        return pclTour;
    }

    protected void setPlanningRequest(PlanningRequest planningRequest) {
        pclPlanningRequest.setPlanningRequest(planningRequest);
    }

    protected void setTour(Tour tour) {
        //System.out.println("setting tour");
        this.pclTour.setTour(tour);
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
        currentState.cancel(this, pclTour.getTour(), window, listOfCommands);
    }

    public void confirm() {
        confirm("");
    }

    public void confirm(String result) {
        currentState.confirm(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), pclTour.getTour(), result, window, listOfCommands);
    }

    public void generateRoadMap() {
        currentState.generateRoadMap(this, pclTour.getTour(), window);
    }

    public void deleteRequest(Long idRequest) {
        currentState.deleteRequest(this, pclCityMap.getCityMap(), pclTour.getTour(), pclTour.getTour().getRequests().get(idRequest), window, listOfCommands);
    }

    public void modifyClick(Long id, String type, int stepIndex) {
        currentState.modifyClick(this, id, type, stepIndex, window);
    }

    public void addRequest(Long id) {
        currentState.addRequest(this, pclCityMap.getCityMap(), pclTour.getTour(), id, window);
    }

    /**
     * Method called by window after a click on the button "Undo"
     */
    public void undo() {
        currentState.undo(this, listOfCommands, window, pclTour.getTour());
    }

    /**
     * Method called by window after a click on the button "Redo"
     */
    public void redo() {
        currentState.redo(this, listOfCommands, window, pclTour.getTour());
    }

    public Window getWindow() {
        return window;
    }

    public void keystroke(KeyCode code, boolean controlDown) {
        currentState.keystroke(this, code, window, controlDown);
    }

}