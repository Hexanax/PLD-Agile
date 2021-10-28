package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

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
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if (importFile != null) {
                XMLDeserializer.load(planningRequest, cityMap, importFile);
                window.renderPlanningRequest(planningRequest);
                controller.setCurrentState(controller.requestsDisplayedState);
            } else {
                controller.setCurrentState(controller.mapDisplayedState);
            }
        } catch(Exception e) {
            window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }



}
