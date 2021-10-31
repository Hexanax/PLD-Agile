package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

import static java.awt.SystemColor.window;

public class ModifyTourState implements State{
    @Override
    public void cancel(Controller controller, Tour tour,Tour modifyTour, Window window, ListOfCommands listOfCdes) {
        window.clearTour();
        window.renderTour(tour.getIntersections());
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        listOfCdes.reset();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest,Tour tour, Tour modifyTour,String result, Window window, ListOfCommands listOfCdes) {
        controller.validModifyTour();
        window.hideModifyMenu();
        listOfCdes.reset();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void deleteRequest(Controller controller,CityMap citymap, Tour tour,Tour modifyTour, Request request, Window window) {
        if(modifyTour.getRequests().size()==1){
            window.showWarningAlert("Tour can't be empty", "You can't delete the last request of the tour", null);
        } else {
            controller.setCurrentState(controller.deleteRequestState1);
            window.clearTour();
            //TODO make map clickable
            window.activeRowListener();
            window.showWarningAlert("How to delete a request", null, "Select the pickup or the delivery address on the map or by double tap in the list of the request you want to delete");
        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour, Tour modifyTour,Long intersectionID,  Window window) {
        controller.setCurrentState(controller.addRequestState1);
        window.clearTour();
        //TODO make map clickable
        window.activeRowListener();
        window.showWarningAlert("How to add a request", null, "Select the depot, a pickup or a delivery after which you want to place the pickup of your new request");

    }

    @Override
    public void undo(ListOfCommands listOfCdes, Window window, Tour tour, Tour modifyTour) {
        listOfCdes.undo();
        window.clearTour();
        window.renderTour(modifyTour.getIntersections());
        window.orderListRequests(modifyTour.getSteps(), modifyTour.getRequests(), tour.getDepot());
    }

    @Override
    public void redo(ListOfCommands listOfCdes,Window window, Tour tour, Tour modifyTour){
        listOfCdes.redo();
    }
}
