package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLCityMap;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import javafx.scene.input.KeyCode;

/**
 * This class allows to translate the interactions between the user and the view by managing the states of the application and by calling the right services that will act on the data
 * This is the core of the MCV pattern
 */
public class Controller {

    //The controller and application's state
    private State currentState;
    private final PCLCityMap pclCityMap;
    private final PCLPlanningRequest pclPlanningRequest;
    private final PCLTour pclTour;
    private final Window window;
    private final ListOfCommands listOfCommands;

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

    /**
     * Call once at application launch to instantiate its controller
     */
    public Controller() {
        this.pclCityMap = new PCLCityMap();
        this.pclPlanningRequest = new PCLPlanningRequest();
        this.pclTour = new PCLTour();
        this.listOfCommands = new ListOfCommands();
        this.currentState = initialState;
        this.window = new Window(this);
    }

    /**
     * Allows to get the observable city map
     * @return the observable city map
     */
    public PCLCityMap getPclCityMap() {
        return pclCityMap;
    }

    /**
     * Allows to get the observable planning request
     * @return the observable planning request
     */
    public PCLPlanningRequest getPclPlanningRequest() {
        return pclPlanningRequest;
    }

    /**
     * Allows to get the observable tour
     * @return the observable tour
     */
    public PCLTour getPclTour() {
        return pclTour;
    }

    /**
     * Allow to get the window used to display the view
     * @return the window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Change the current state of the controller
     * @param state the new current state
     */
    protected void setCurrentState(State state) {
        currentState = state;
    }

    /**
     * Update the city map in the observable city map
     * @param cityMap the city map
     */
    protected void setCityMap(CityMap cityMap) {
        pclCityMap.setCityMap(cityMap);
    }

    /**
     * Update the planning request in the observable planning request
     * @param planningRequest the planning request
     */
    protected void setPlanningRequest(PlanningRequest planningRequest) {
        pclPlanningRequest.setPlanningRequest(planningRequest);
    }

    /**
     * Update the tour in the observable tour
     * @param tour the tour
     */
    protected void setTour(Tour tour) {
        this.pclTour.setTour(tour);
    }

    /**
     * Method called by window after a click on the button "Import map"
     */
    public void loadMap() {
        currentState.loadMap(this, window);
    }

    /**
     * Method called by window after a click on the button "Import requests"
     */
    public void loadRequests() {
        currentState.loadRequests(this, pclCityMap.getCityMap(), window);
    }

    /**
     * Method called by window after a click on the button "Compute  tour"
     */
    public void computeTour() {
        currentState.computeTour(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), window);
    }

    /**
     * Method called by window after a right click a click on cancel
     */
    public void cancel() {
        currentState.cancel(this, pclPlanningRequest.getPlanningRequest(), pclTour.getTour(), window, listOfCommands);
    }

    /**
     * Method called by window after a left click or a click on confirm
     */
    public void confirm() {
        currentState.confirm(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), pclTour.getTour(), window, listOfCommands);
    }

    /**
     * Method called by window after a click on the button "Generate the Road Map"
     */
    public void generateRoadMap() {
        currentState.generateRoadMap(this, pclTour.getTour(), window);
    }

    /**
     * Method called by window after a click on the button "Delete Request"
     * @param idRequest the id of the request to delete. It can be null
     */
    public void deleteRequest(Long idRequest) {
        currentState.deleteRequest(this, pclCityMap.getCityMap(), pclPlanningRequest, pclTour, idRequest, window, listOfCommands);
    }

    /**
     * Method called after a left click in mode "modify"
     * @param id the id of the object clicked
     * @param type the type of the object clicked
     * @param stepIndex the position in the steps list of the object clicked
     */
    public void modifyClick(Long id, String type, int stepIndex) {
        currentState.modifyClick(this, pclPlanningRequest.getPlanningRequest(), pclTour.getTour(), id, type, stepIndex, window);
    }

    /**
     * Method called by the window after a click on the button "Add Request
     */
    public void addRequest() {
        currentState.addRequest(this, pclCityMap.getCityMap(), pclPlanningRequest, pclTour, listOfCommands, window);
    }

    /**
     * Method called by window after a click on the button "Undo"
     */
    public void undo() {
        currentState.undo(this, listOfCommands, window);
    }

    /**
     * Method called by window after a click on the button "Redo"
     */
    public void redo() {
        currentState.redo(this, listOfCommands, window);
    }

    /**
     * Method called by the controller after a ctrl+Z or a ctrl+Y
     * @param code the key code
     * @param controlDown true if the key control is active
     */
    public void keystroke(KeyCode code, boolean controlDown) {
        currentState.keystroke(this, code, controlDown);
    }

    /**
     * Method called after a click on the intersection
     * @param id the id of the intersection clicked
     */
    public void intersectionClick(long id) {
        currentState.intersectionClick(this, pclCityMap.getCityMap(), pclPlanningRequest.getPlanningRequest(), id, window);
    }
}
