package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;

public class InitialState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        try {
            File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
            if (importFile != null){
                window.addStateFollow("Loading the map ...");
                XMLDeserializer.load(citymap, importFile);
                window.clearMap();
                window.renderCityMap(citymap);
                window.centerMap(citymap);
                window.updateMapFileName(importFile.getName());
                controller.setCurrentState(controller.mapDisplayedState);
                window.addStateFollow("Map loaded");
            } else {
                controller.setCurrentState(controller.initialState);
            }
        } catch(Exception e) {
            window.addStateFollow("Error when reading the XML map file : " + e.getMessage());
        }
    }
}
