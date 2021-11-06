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
 *  RequestsOverwrite1State is the state when map & requests & tour
 *  are loaded in the system and the user wants to load new requests
 */
public class RequestsOverwrite2State implements State{
    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if(importFile != null) {
                window.addStateFollow("Loading the new requests ...");
                PlanningRequest newPlanningRequest = XMLDeserializer.load(cityMap, importFile);
                controller.setTour(new Tour());
                controller.setPlanningRequest(newPlanningRequest);
                window.updateRequestFileName(importFile.getName());
                controller.setCurrentState(controller.requestsDisplayedState);
                window.addStateFollow("New requests loaded");
            } else {
                controller.setCurrentState(controller.tourComputedState);
            }
        } catch(Exception e) {
            window.addWarningStateFollow("Error when reading the XML requests file " +e.getMessage());
            controller.setCurrentState(controller.tourComputedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour,String result, Window window,ListOfCommands listOfCdes) {
        this.loadRequests(controller, citymap, window);
    }

    @Override
    public void cancel(Controller controller,Tour tour,Window window,ListOfCommands listOfCdes) {
        controller.setCurrentState(controller.tourComputedState);
    }
}
