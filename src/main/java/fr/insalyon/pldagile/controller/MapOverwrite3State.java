package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

public class MapOverwrite3State implements State{
    @Override
    public void loadMap(Controller controller, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if(importFile != null) {
                CityMap newMap = XMLDeserializer.load(importFile);
                controller.setCityMap(newMap);
                controller.setPlanningRequest(new PlanningRequest());
                controller.setTour(new Tour());
                window.clearRequest();
                window.updateMapFileName(importFile.getName());
                controller.setCurrentState(controller.mapDisplayedState);
            } else {
                controller.setCurrentState(controller.tourComputedState);
            }
        } catch(Exception e) {
            window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            controller.setCurrentState(controller.tourComputedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour,String result, Window window,ListOfCommands listOfCdes) {
        this.loadMap(controller, window);
    }

    @Override
    public void cancel(Controller controller,Tour tour,Window window,ListOfCommands listOfCdes) {
        controller.setCurrentState(controller.tourComputedState);
    }
}
