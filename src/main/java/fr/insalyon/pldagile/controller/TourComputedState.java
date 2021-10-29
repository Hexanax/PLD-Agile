package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.HTMLSerializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;

import java.io.File;


public class TourComputedState implements State{
    @Override
    public void loadMap(Controller controller, CityMap citymap, Window window) {
        controller.setCurrentState(controller.mapOverwrite3State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        controller.setCurrentState(controller.requestsOverwrite2State);
        window.showValidationAlert("Load new requests",
                "Are you sure you want to load new requests ? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void modify(Controller controller, Window window) {
        controller.setCurrentState(controller.modifyTourState);
        controller.initializaModifyTour();
        window.showModifyMenu();
    }

    @Override
    public void generateRoadMap(Controller controller, Tour tour, Window window) {
        try {
            File html = XMLFileOpener.getInstance().open(FileChooseOption.SAVE);
            if(html != null){
                HTMLSerializer.renderHTMLroadMap(tour, html);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
