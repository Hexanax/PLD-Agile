package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.XMLDeserializer;

public class MapOverwrite1State implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        try {
            CityMap newMap = new CityMap();
            XMLDeserializer.load(newMap);
            controller.setCitymap(newMap);
            window.clearMap();
            window.renderCityMap(newMap);
        } catch(Exception e) {
            if(!e.getMessage().equals("cancel")){
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
        } finally {
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,  Window window) {
        this.loadMap(controller, citymap, window);
    }

    @Override
    public void cancel(Controller controller) {
        controller.setCurrentState(controller.mapDisplayedState);
    }
}
