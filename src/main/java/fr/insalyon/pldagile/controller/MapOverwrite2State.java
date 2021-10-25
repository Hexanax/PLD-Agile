package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class MapOverwrite2State implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        try {
            CityMap newMap = new CityMap();
            XMLDeserializer.load(newMap);
            controller.setCitymap(newMap);
            controller.setPlanningRequest(new PlanningRequest());
            window.clearRequest();
            window.clearMap();
            window.renderCityMap(newMap);
            controller.setCurrentState(controller.mapDisplayedState);
        } catch(Exception e) {
            if(!e.getMessage().equals("cancel")){
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
            controller.setCurrentState(controller.requestsDisplayedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Window window) {
        this.loadMap(controller, citymap, window);
    }

    @Override
    public void cancel(Controller controller) {
        controller.setCurrentState(controller.requestsDisplayedState);
    }
}
