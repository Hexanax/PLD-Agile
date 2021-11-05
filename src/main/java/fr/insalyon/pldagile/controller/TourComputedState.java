package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.HTMLSerializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;
import javafx.scene.input.KeyCode;

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
    public void generateRoadMap(Controller controller, Tour tour, Window window) {
        try {
            window.addStateFollow("Road Map calculation ...");
            File html = XMLFileOpener.getInstance().open(FileChooseOption.SAVE);
            if(html != null){
                HTMLSerializer.renderHTMLroadMap(tour, html);
                window.addStateFollow("Road Map rendered");
            }
        } catch (Exception e) {
            window.addStateFollow(e.getMessage());
        }

    }

    @Override
    public void deleteRequest(Controller controller,CityMap citymap, Tour tour, Request request, Window window,ListOfCommands listOfCdes) {
        if(tour.getRequests().size()==1){
            window.addStateFollow("Tour can't be empty \n You can't delete the last request of the tour");
        } else {
            controller.setCurrentState(controller.deleteRequestState1);
            window.disableEventListener();
            window.disableItemListener();
            window.clearTour();
            window.activeRowListener();
            window.activeRequestIntersectionsListener();
            window.addStateFollow("How to delete a request : Select the pickup or the delivery address on the map or by double tap in the list of the request you want to delete");
        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour,Long intersectionID,  Window window) {
        controller.setCurrentState(controller.addRequestState1);
        window.disableEventListener();
        window.disableItemListener();
        window.clearTour();
        window.activeRowListener();
        window.activeRequestIntersectionsListener();
        window.addStateFollow("How to add a request : Select the depot, a pickup or a delivery after which you want to place the pickup of your new request");
    }


    @Override
    public void undo(ListOfCommands listOfCdes, Window window, Tour tour) {

        listOfCdes.undo();

        window.clearTour();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.addStateFollow("Undo");
    }

    @Override
    public void redo(ListOfCommands listOfCdes,Window window, Tour tour){
        listOfCdes.redo();
        window.clearTour();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
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
