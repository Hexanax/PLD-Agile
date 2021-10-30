package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState5 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window) {
        window.renderTour(tour.getIntersections());
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, String result, Window window) {
        window.showWarningAlert("How to add a request", null, "Please first select the delivery address of your new request");
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
        } else if(intersectionId == controller.pickupToAdd.getValue().getIntersection().getId()){
            window.showWarningAlert("How to add a request", "You can't add a request with the same address for pickup and delivery", "please try again");
        } else {
            controller.deliveryToAdd = new Pair<Integer, Delivery>(controller.deliveryToAdd.getKey(), new Delivery(intersection, 300));
            window.disableEventListener();
            controller.setCurrentState(controller.addRequestState6);
            window.showInputAlert("Delivery Duration", "Please select the duration in second", "Delivery duration :");
        }
    }
}
