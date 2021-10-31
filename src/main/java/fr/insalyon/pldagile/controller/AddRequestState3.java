package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public class AddRequestState3 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window,ListOfCommands listOfCdes) {
        window.renderTour(tour.getIntersections());
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        listOfCdes.reset();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, String result, Window window,ListOfCommands listOfCdes) {
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
            controller.setCurrentState(controller.addRequestState4);
            //TODO make map clickable
            window.activeRowListener();
            window.showWarningAlert("How to add a request", null, "Select the depot, a pickup or a delivery after which you want to place the delivery of your new request");
        } else {
            window.showWarningAlert("Error","Wrong format, number must be a positive Integer" ,null);
            window.showInputAlert("Pickup Duration", "Please select the duration in second", "Pickup duration :");
        }
    }
}
