package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TourComputedState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        controller.setCurrentState(controller.mapOverwrite3State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        controller.setCurrentState(controller.requestsOverwrite2State);
        window.showValidationAlert("Load new requests",
                "Are you sure you want to load new requests ? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void modify(Controller controller, Window window) {
        controller.setCurrentState(controller.modifyTourState);
        System.out.println("modify");
        window.showModifyMenu();
    }

    @Override
    public void generateRoadMap(Controller controller, Tour tour, Window window) {
        TourBuilderV2 tourbuilder = new TourBuilderV2();
        Map<Long,List<Pair<Address,Long>>> specificIntersections = tourbuilder.buildSpecificIntersections(tour);


    }
}
