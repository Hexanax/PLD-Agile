package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class TourComputedState implements State{
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
            //TODO : Delete current request and tour
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
            //TODO : delete current request and tour and load news
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.requestsDisplayedState);
        }
    }
}
