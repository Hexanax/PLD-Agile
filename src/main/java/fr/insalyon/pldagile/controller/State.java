package fr.insalyon.pldagile.controller;


import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import javafx.scene.input.KeyCode;

/**
 * This interface gives all functionalities that the controller can deal with
 */
public interface State {

    /**
     * Method called by the controller after a click on "Import map"
     * @param controller the controller
     * @param window the window
     * @param l the current list of commands
     */
    public default void loadMap(Controller controller, Window window, ListOfCommands l) {};

    /**
     * Method called by the controller after a click on "Import Requests"
     * @param controller the controller
     * @param cityMap the city map
     * @param window the window
     * @param l the current list of commands
     */
    public default void loadRequests(Controller controller, CityMap cityMap, Window window, ListOfCommands l){};

    /**
     * Method called by the controller after a click on "Compute Tour"
     * @param controller the controller
     * @param cityMap the city map
     * @param planningRequest the planning request
     * @param window the window
     */
    public default void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window, boolean slowModeActivated){};

    /**
     * Method called by the controller after a left click or a click on confirm
     * @param controller the controller
     * @param citymap the city map
     * @param planningRequest the planning request
     * @param tour the tour
     * @param window the window
     * @param l the current list of commands
     */
    public default void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, Window window, ListOfCommands l){};

    /**
     * Method called by the controller after a right click or a click on cancel
     * @param controller the controller
     * @param planningRequest the planning request
     * @param tour the tour
     * @param window the window
     * @param l the current list of commands
     */
    public default void cancel(Controller controller,PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l){};

    /**
     * Method called by the controller after a click on "Generate the road map"
     * @param controller the controller
     * @param tour the tour
     * @param window the window
     */
    public default void generateRoadMap(Controller controller, Tour tour, Window window){};

    /**
     * Method called by the controller after a click in modify mode
     * @param controller the controller
     * @param planningRequest the planning request
     * @param tour the tour
     * @param id the id of the element clicked
     * @param type the type of the element clicked
     * @param stepIndex the index in case of the element is a request
     * @param window the window
     */
    public default void modifyClick(Controller controller, PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window){};

    /**
     * Method called bu the controller after a click on "Delete request"
     * @param controller the controller
     * @param citymap the citymap
     * @param pclPlanningRequest the observable planning request
     * @param pcltour the observable tour
     * @param idRequest the request to delete
     * @param window the window
     * @param listOfCdes the current list of commands
     */
    public default void deleteRequest(Controller controller,CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, Long idRequest, Window window,ListOfCommands listOfCdes){};

    /**
     * Method calledby the controller after a click on "Add request"
     * @param controller the controller
     * @param citymap the city map
     * @param pclPlanningRequest the observable planning request
     * @param pcltour the observable tour
     * @param l the current list of commands
     * @param window the window
     */
    public default void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window){};

    /**
     * Method called by the controller after a click on the button "Undo"
     * @param controller the controller
     * @param l the current list of commands
     * @param w the window
     */
    public default void undo(Controller controller, ListOfCommands l, Window w){};

    /**
     * Method called by the controller after a click on the button "Redo"
     * @param controller the controller
     * @param l the current list of commands
     * @param w the window
     */
    public default void redo(Controller controller, ListOfCommands l,Window w){};

    /**
     * Method called by the controller after a ctrl+Z or a ctrl+Y
     * @param controller the controller
     * @param code the code
     * @param isControlDown check if the ctrl is down
     */
    public default void keystroke(Controller controller, KeyCode code, boolean isControlDown){};

    /**
     * Method called by the controller after a click on an intersection
     * @param controller the controller
     * @param cityMap the city map
     * @param planningRequest the planning request
     * @param intersectionID the intersection id selected
     * @param window the window
     */
    public default void intersectionClick(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Long intersectionID, Window window){};

}