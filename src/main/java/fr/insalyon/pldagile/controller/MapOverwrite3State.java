package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class MapOverwrite3State implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        try {
            CityMap newMap = new CityMap();
            XMLDeserializer.load(newMap);
            controller.setCitymap(newMap);
            controller.setPlanningRequest(new PlanningRequest());
            controller.setTour(new Tour());
            window.clearRequest();
            window.clearMap();
            window.renderCityMap(newMap);
            window.centerMap(newMap);
            controller.setCurrentState(controller.mapDisplayedState);
        } catch(Exception e) {
            if(!e.getMessage().equals("cancel")){
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
            controller.setCurrentState(controller.tourComputedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Window window) {
        this.loadMap(controller, citymap, window);
    }

    @Override
    public void cancel(Controller controller) {
        controller.setCurrentState(controller.tourComputedState);
    }
}
