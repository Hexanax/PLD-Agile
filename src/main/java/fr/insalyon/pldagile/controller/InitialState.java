package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.PickyApplication;
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
            XMLDeserializer.load(citymap);
            window.clearMap();
            window.renderCityMap(citymap);
            controller.setCurrentState(controller.mapDisplayedState);
        } catch(Exception e) {
            if(e.getMessage().equals("cancel")){
                controller.setCurrentState(controller.initialState);
            } else {
                window.showWarningAlert("Error when reading the XML map file",e.getMessage() ,null);
            }
        }
    }
}