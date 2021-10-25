package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class RequestsOverwrite2State implements State{
    @Override
    public void loadRequests(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        try {
            PlanningRequest newPlanningRequest = new PlanningRequest();
            XMLDeserializer.load(newPlanningRequest, cityMap);
            controller.setPlanningRequest(newPlanningRequest);
            controller.setTour(new Tour());
            window.clearRequest();
            window.clearTour();
            window.renderPlanningRequest(newPlanningRequest);
            controller.setCurrentState(controller.requestsDisplayedState);
        } catch(Exception e) {
            if(!e.getMessage().equals("cancel")){
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
            controller.setCurrentState(controller.tourComputedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Window window) {
        this.loadRequests(controller, citymap,planningRequest, window);
    }

    @Override
    public void cancel(Controller controller) {
        controller.setCurrentState(controller.tourComputedState);
    }
}
