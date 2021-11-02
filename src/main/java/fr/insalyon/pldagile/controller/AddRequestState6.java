package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public class AddRequestState6 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window) {
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, String result, Window window) {
        int duration = 0;
        boolean valid = false;
        try {
            duration = Integer.parseInt(result);
            if(duration>=0){
                valid = true;
            }
        } catch (NumberFormatException e){
            valid = false;
        }

        if(valid){
            controller.pickupToAdd.getValue().setDuration(duration);
            controller.setCurrentState(controller.addRequestState7);
            window.showValidationAlert("Add request ?",
                    "Are you sure you want to add the request from intersection no" +controller.pickupToAdd.getValue().getIntersection().getId() + "with a pickup time of "+controller.pickupToAdd.getValue().getDuration() +" to intersection no "+ controller.deliveryToAdd.getValue().getIntersection().getId()+ "with a delivery time of "+controller.pickupToAdd.getValue().getDuration(),
                    null);
        } else {
            window.showWarningAlert("Error","Wrong format, number must be a positive Integer" ,null);
            window.showInputAlert("Delivery Duration", "Please select the duration in second", "Delivery duration :");
        }
    }
}
