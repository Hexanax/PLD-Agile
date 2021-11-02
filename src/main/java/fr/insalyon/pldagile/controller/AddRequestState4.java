package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState4 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window) {
        window.renderTour(tour);
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
        if(type=="Depot" && stepIndex!=0)
        {
            window.showWarningAlert("How to add a request", "You can't add a request after the arrival of the tour", null);
        } else if(stepIndex < controller.pickupToAdd.getKey()) {
            window.showWarningAlert("How to add a request", "You can't add a request with delivery before pickups", null);
        }
        else {
            controller.deliveryToAdd = new Pair<Integer, Delivery>(stepIndex, null);
            window.showWarningAlert("How to add a request", "Please select on the map the intersection of your delivery", null);
            window.disableEventListener();
            window.activeMapIntersectionsListener();
            controller.setCurrentState(controller.addRequestState5);
        }

    }
}
