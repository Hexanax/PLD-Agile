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
 * TourComputedState is the state when map & requests & tour computed
 * are loaded in the system and the user
 */
public class TourComputedState implements State{
    @Override
    public void loadMap(Controller controller, Window window, ListOfCommands l) {
        controller.setCurrentState(controller.mapOverwrite3State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded and lose the calculated tour");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window, ListOfCommands l) {
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
            if (html != null) {
                HTMLSerializer.renderHTMLroadMap(tour, html);
                window.addStateFollow("Road Map rendered");
            }
        } catch (Exception e) {
            window.addWarningStateFollow(e.getMessage());
        }
    }

    @Override
    public void deleteRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, Long idRequest, Window window, ListOfCommands listOfCdes) {
        if(pclPlanningRequest.getPlanningRequest().getRequests().size()==1){
            window.addWarningStateFollow("Tour can't be empty \n You can't delete the last request of the tour");
        } else {
            controller.setCurrentState(controller.deleteRequestState1);
            window.deleteView();
            window.addStateFollow("How to delete a request : Select the pickup or the delivery address on the map or by double tap in the list of the request you want to delete");
        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window) {
        controller.setCurrentState(controller.addRequestState1);
        window.addStateFollow("Left click on the pickup intersection or right click to cancel");
        window.addView();
    }


    @Override
    public void undo(Controller controller, ListOfCommands listOfCdes, Window window) {
        try{
            listOfCdes.undo();
            window.addStateFollow("Undo");
        } catch (Exception e) {
            e.printStackTrace();
            window.addWarningStateFollow(e.getMessage());
        }

    }

    @Override
    public void redo(Controller controller, ListOfCommands listOfCdes, Window window) {
        try {
            listOfCdes.redo();
            window.addStateFollow("Redo");
        } catch (Exception e) {
            window.addWarningStateFollow(e.getMessage());
        }
    }

    @Override
    public void keystroke(Controller controller, KeyCode code, boolean isControlDown) {
        if (isControlDown) {
            if (code == KeyCode.Z) {
                controller.undo();
            }

            if (code == KeyCode.Y) {
                controller.redo();
            }
        }
    }

}
