package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState2 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window) {
        window.renderTour(tour.getIntersections());
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour,String result, Window window) {
        window.showWarningAlert("How to add a request", null, "Please first select the pickup address of your new request");
    }

    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        controller.addRequest(id);
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour, Tour modifyTour, Long intersectionId, Window window) {
        Intersection intersection = citymap.getIntersection(intersectionId);
        if(intersection == null){
            window.showWarningAlert("How to add a request", "Intersection number unknown, please try again", null);
        } else {
            controller.pickupToAdd = new Pair<Integer, Pickup>(controller.pickupToAdd.getKey(), new Pickup(intersection, 300));
            window.disableEventListener();
            controller.setCurrentState(controller.addRequestState3);
            window.showInputAlert("Pickup Duration", "Please select the duration in second", "Pickup duration :");
        }
    }
}
