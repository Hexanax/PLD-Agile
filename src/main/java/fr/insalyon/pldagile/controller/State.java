package fr.insalyon.pldagile.controller;


import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;

public interface State {

    public default void loadMap(Controller controller, CityMap citymap, Window window) {};

    public default void loadRequests(Controller controller,CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, Tour modifyTour, String result, Window window, ListOfCommands l){};

    public default void cancel(Controller controller,Tour tour,Tour modifyTour, Window window, ListOfCommands l){};

    public default void modify(Controller controller, Window window){};

    public default void generateRoadMap(Controller controller, Tour tour, Window window){};

    public default void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window){};

    public default void deleteRequest(Controller controller,CityMap citymap, Tour tour,Tour modifyTour, Request request, Window window,ListOfCommands listOfCdes){};

    public default void addRequest(Controller controller, CityMap citymap, Tour tour, Tour modifyTour, Long intersectionID, Window window){};

    /**
     * Method called by the controller after a click on the button "Undo"
     * @param l the current list of commands
     */
    public default void undo(ListOfCommands l, Window w, Tour tour, Tour modifyTour){};

    /**
     * Method called by the controller after a click on the button "Redo"
     * @param l the current list of commands
     */
    public default void redo(ListOfCommands l,Window w, Tour tour, Tour modifyTour){};
}