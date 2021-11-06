package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.HTMLSerializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;
import javafx.scene.input.KeyCode;

import java.io.File;

/**
 *  TourComputedState is the state when map & requests & tour computed
 *  are loaded in the system and the user
 */
public class TourComputedState implements State{
    @Override
    public void loadMap(Controller controller, Window window) {
        controller.setCurrentState(controller.mapOverwrite3State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window) {
        controller.setCurrentState(controller.requestsOverwrite2State);
        window.showValidationAlert("Load new requests",
                "Are you sure you want to load new requests ? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }



    @Override
    public void generateRoadMap(Controller controller, Tour tour, Window window) {
        try {
            window.addStateFollow("Road Map calculation ...");
            File html = XMLFileOpener.getInstance().open(FileChooseOption.SAVE);
            if(html != null){
                HTMLSerializer.renderHTMLroadMap(tour, html);
                window.addStateFollow("Road Map rendered");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteRequest(Controller controller, CityMap citymap, PCLTour pcltour, Request request, Window window, ListOfCommands listOfCdes) {
        if(pcltour.getTour().getRequests().size()==1){
            window.addWarningStateFollow("Tour can't be empty \n You can't delete the last request of the tour");
        } else {
            controller.setCurrentState(controller.deleteRequestState1);
            //TODO Display only the planning request
            window.clearTour();
            window.addStateFollow("How to delete a request : Select the pickup or the delivery address on the map or by double tap in the list of the request you want to delete");
        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window) {
        controller.setCurrentState(controller.addRequestState1);
        window.addStateFollow("Left click on the intersection where the pickup will take place or right click to cancel");
        //TODO Display only the intersection
        window.clearTour();

    }


    @Override
    public void undo(Controller controller, ListOfCommands listOfCdes, Window window, Tour tour) {
        listOfCdes.undo();
        Tour modify = new Tour(tour);
        controller.setTour(modify);
        window.addStateFollow("Undo");
    }

    @Override
    public void redo(Controller controller, ListOfCommands listOfCdes,Window window, Tour tour){
        listOfCdes.redo();
        controller.setTour(tour);
        window.addStateFollow("Redo");
    }

    @Override
    public void keystroke(Controller controller, KeyCode code, Window window, boolean isControlDown) {
        if(isControlDown){
            if(code == KeyCode.Z){
                controller.undo();
            }

            if(code== KeyCode.Y){
                controller.redo();
            }
        }
    }


}
