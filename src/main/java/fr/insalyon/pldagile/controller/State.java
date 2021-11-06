package fr.insalyon.pldagile.controller;


import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import javafx.scene.input.KeyCode;

public interface State {

    /**
     * load a map into the application
     * @param controller
     * @param window
     */
    public default void loadMap(Controller controller, Window window) {};

    /**
     * Load requests into the application
     * @param controller
     * @param cityMap
     * @param window
     */
    public default void loadRequests(Controller controller, CityMap cityMap, Window window){};

    /**
     * compute the tour from the CityMap and PlanningRequest in parameters
     * @param controller
     * @param cityMap
     * @param planningRequest
     * @param window
     */
    public default void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, String result, Window window, ListOfCommands l){};

    public default void cancel(Controller controller,PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l){};

    public default void generateRoadMap(Controller controller, Tour tour, Window window){};

    public default void modifyClick(Controller controller, PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window){};

    public default void deleteRequest(Controller controller,CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, Request request, Window window,ListOfCommands listOfCdes){};


    public default void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window){};

    /**
     * Method called by the controller after a click on the button "Undo"
     * @param l the current list of commands
     */
    public default void undo(Controller controller, ListOfCommands l, Window w, Tour tour){};

    /**
     * Method called by the controller after a click on the button "Redo"
     * @param l the current list of commands
     */
    public default void redo(Controller controller, ListOfCommands l,Window w, Tour tour){};


    public default void keystroke(Controller controller, KeyCode code, Window window, boolean isControlDown){};

    public default void entryAction(Controller controller, PCLTour pclTour){};

    public default void intersectionClick(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Long intersectionID, Window window){};

}