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
            //TODO : Deal with cancel button
            XMLDeserializer.load(citymap);
        } catch(Exception e) {
            //TODO : Display alert Message
            e.printStackTrace();
        } finally {
            Window.clearMap();
            Window.renderMapAndRequests();
            controller.setCurrentState(controller.mapDisplayedState);
        }
    }
}
