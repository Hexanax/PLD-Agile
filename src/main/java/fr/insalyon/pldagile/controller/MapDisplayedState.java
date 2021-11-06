package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

/**
 * MapDisplayedState is the state when only the map has been loaded in the application
 */
public class MapDisplayedState implements State{
    @Override
    public void loadMap(Controller controller, Window window) {
        controller.setCurrentState(controller.mapOverwrite1State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                null);

    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if (importFile != null) {
                window.addStateFollow("Loading the requests ...");
                PlanningRequest newPlanningRequest = XMLDeserializer.load(cityMap, importFile);
                controller.setPlanningRequest(newPlanningRequest);
                window.updateRequestFileName(importFile.getName());
                window.activeItemListener();
                controller.setCurrentState(controller.requestsDisplayedState);
                window.addStateFollow("Requests loaded");
            } else {
                controller.setCurrentState(controller.mapDisplayedState);
            }
        } catch(Exception e) {
            window.addWarningStateFollow("Error when reading the XML requests file " +e.getMessage());
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }



}
