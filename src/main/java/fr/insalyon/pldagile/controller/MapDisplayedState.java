package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class MapDisplayedState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        controller.setCurrentState(controller.mapOverwrite1State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                null);

    }

    @Override
    public void loadRequests(Controller controller,CityMap cityMap, PlanningRequest planningRequest, Window window) {
        try {
            XMLDeserializer.load(planningRequest, cityMap);
            window.renderPlanningRequest(planningRequest);
            controller.setCurrentState(controller.requestsDisplayedState);
        } catch(Exception e) {
            if(e.getMessage().equals("cancel")){
                controller.setCurrentState(controller.initialState);
            } else {
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }



}
