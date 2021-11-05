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

    @Override
    public void deleteRequest(Controller controller,CityMap citymap, Tour tour, Request request, Window window,ListOfCommands listOfCdes) {
        if(tour.getRequests().size()==1){
            window.showWarningAlert("Tour can't be empty", "You can't delete the last request of the tour", null);
        } else {
            controller.setCurrentState(controller.deleteRequestState1);
            window.disableEventListener();
            window.disableItemListener();
            window.clearTour();
            window.activeRowListener();
            window.activeRequestIntersectionsListener();
            window.showWarningAlert("How to delete a request", null, "Select the pickup or the delivery address on the map or by double tap in the list of the request you want to delete");
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
        window.showWarningAlert("How to add a request", null, "Select the depot, a pickup or a delivery after which you want to place the pickup of your new request");

    }


    @Override
    public void undo(ListOfCommands listOfCdes, Window window, Tour tour) {

        listOfCdes.undo();

        window.clearTour();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
    }

    @Override
    public void redo(ListOfCommands listOfCdes,Window window, Tour tour){
        listOfCdes.redo();
        window.clearTour();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
    }


}
