package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class MapDisplayedState implements State{
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
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }

    @Override
    public void loadRequests(Controller controller,CityMap cityMap, PlanningRequest planningRequest, Window window) {
        try {
            //TODO : Deal with cancel button
            XMLDeserializer.load(planningRequest, cityMap);
        } catch(Exception e) {
            //TODO : Display alert Message
            e.printStackTrace();
        } finally {
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.requestsDisplayedState);
        }
    }
}
