package fr.insalyon.pldagile.controller;


import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.event.EventTarget;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public interface State {

    public default void loadMap(Controller controller, CityMap citymap, Window window) {};

    public default void loadRequests(Controller controller,CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, String result, Window window, ListOfCommands l){};

    public default void cancel(Controller controller,Tour tour, Window window, ListOfCommands l){};

    public default void generateRoadMap(Controller controller, Tour tour, Window window){};

    public default void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window){};

    public default void deleteRequest(Controller controller,CityMap citymap, Tour tour, Request request, Window window,ListOfCommands listOfCdes){};

    public default void addRequest(Controller controller, CityMap citymap, Tour tour, Long intersectionID, Window window){};

    /**
     * Method called by the controller after a click on the button "Undo"
     * @param l the current list of commands
     */
    public default void undo(ListOfCommands l, Window w, Tour tour){};

    /**
     * Method called by the controller after a click on the button "Redo"
     * @param l the current list of commands
     */
    public default void redo(ListOfCommands l,Window w, Tour tour){};

    public default void keystroke(Controller controller, KeyCode code, Window window, boolean isControlDown) {}

    public default void leftClick(Controller controller, MouseEvent event, Window window) {}


}