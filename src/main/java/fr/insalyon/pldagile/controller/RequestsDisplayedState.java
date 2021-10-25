package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.tsp.TourBuilderV1;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

import java.util.List;

public class RequestsDisplayedState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        //TODO : Ask for confirmation
        try {
            //TODO : Deal with cancel button
            XMLDeserializer.load(citymap);
        } catch(Exception e) {
            //TODO : Display alert Message
            e.printStackTrace();
        } finally {
            //TODO : Delete current request
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        //TODO : Ask for confirmation
        try {
            //TODO : Deal with cancel button
            XMLDeserializer.load(planningRequest, cityMap);
        } catch(Exception e) {
            //TODO : Display alert Message
            e.printStackTrace();
        } finally {
            //TODO : delete current request and load news
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.requestsDisplayedState);
        }
    }

    @Override
    public void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Tour tour, Window window) {
        //TODO change ALL
        System.out.println("Render tour called");
        TourBuilderV1 tourBuilderV1 = new TourBuilderV1();
        List<Long> intersectionIds = tourBuilderV1.buildTour(planningRequest, cityMap); //TODO Change with segments from TourBuilder
        Window.renderTour(intersectionIds);
        controller.setCurrentState(controller.tourComputedState);
    }
}
