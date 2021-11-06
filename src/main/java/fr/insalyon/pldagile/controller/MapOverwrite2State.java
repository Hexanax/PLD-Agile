package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

/**
 * MapOverwrite2State is the state when map & requests are loaded in the system and the user wants to load a new map
 */
public class MapOverwrite2State implements State{
    @Override
    public void loadMap(Controller controller, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if(importFile != null) {
                window.addStateFollow("Loading the new map ...");
                CityMap newCityMap = XMLDeserializer.load(importFile);
                controller.setCityMap(newCityMap);
                controller.setPlanningRequest(new PlanningRequest());
                window.updateMapFileName(importFile.getName());
                controller.setCurrentState(controller.mapDisplayedState);
                window.addStateFollow("New map loaded");
            } else {
                controller.setCurrentState(controller.requestsDisplayedState);
            }
        } catch(Exception e) {
            window.addWarningStateFollow("Error when reading the XML map file : " + e.getMessage());
            controller.setCurrentState(controller.requestsDisplayedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour,String result, Window window,ListOfCommands listOfCdes) {
        this.loadMap(controller, window);
    }

    @Override
    public void cancel(Controller controller, Tour tour, Window window,ListOfCommands listOfCdes) {
        controller.setCurrentState(controller.requestsDisplayedState);
    }
}
