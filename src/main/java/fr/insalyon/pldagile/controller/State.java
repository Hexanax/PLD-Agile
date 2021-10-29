package fr.insalyon.pldagile.controller;


import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public interface State {

    public default void loadMap(Controller controller, CityMap citymap, Window window) {};

    public default void loadRequests(Controller controller,CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window){};

    public default void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, Tour modifyTour, Window window){};

    public default void cancel(Controller controller,Tour tour,Tour modifyTour, Window window){};

    public default void modify(Controller controller, Window window){};

    public default void generateRoadMap(Controller controller, Tour tour, Window window){};

    public default void deleteRequest(Controller controller,CityMap citymap, Tour tour,Tour modifyTour, Request request, Window window){};
}