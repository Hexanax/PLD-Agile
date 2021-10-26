package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.tsp.TourBuilderV1;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

import java.util.List;



public class RequestsDisplayedState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        controller.setCurrentState(controller.mapOverwrite2State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        controller.setCurrentState(controller.requestsOverwrite1State);
        window.showValidationAlert("Load new requests",
                "Are you sure you want to load new requests ? ",
                "This will remove the requests already loaded");
    }

    @Override
    public void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Tour tour, Window window) {
        //TODO change ALL
        System.out.println("Render tour called");
        TourBuilderV2 tourBuilderV2 = new TourBuilderV2();
        tourBuilderV2.buildTour(planningRequest, cityMap, tour); //TODO Change with segments from TourBuilder

        window.renderTour(tour.getIntersections());
        controller.setCurrentState(controller.tourComputedState);
    }
}
