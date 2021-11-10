package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

/**
 * InitialState is the state when the application is launched.
 */
public class InitialState implements State {
    @Override
    public void loadMap(Controller controller, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if (importFile != null) {
                window.addStateFollow("Loading the map ...");

                //Service call to read an xml file
                CityMap newCityMap = XMLDeserializer.load(importFile);
                controller.setCityMap(newCityMap);
                window.updateMapFileName(importFile.getName());
                controller.setCurrentState(controller.mapDisplayedState);
                window.addStateFollow("Map loaded");
            }
        } catch (Exception e) {
            window.addWarningStateFollow("Error when reading the XML map file : " + e.getMessage());
        }
    }
}
