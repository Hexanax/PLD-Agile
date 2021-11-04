package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

public class MapOverwrite1State implements State{
    @Override
    public void loadMap(Controller controller, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if (importFile != null) {
                CityMap newCityMap = XMLDeserializer.load(importFile);
                controller.setCityMap(newCityMap);
                window.updateMapFileName(importFile.getName());
            }
        } catch(Exception e) {
            window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
        } finally {
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour,String result,  Window window,ListOfCommands listOfCdes) {
        this.loadMap(controller, window);
    }

    @Override
    public void cancel(Controller controller, Tour tour, Window window,ListOfCommands listOfCdes) {
        controller.setCurrentState(controller.mapDisplayedState);
    }
}
