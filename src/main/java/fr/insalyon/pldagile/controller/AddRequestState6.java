package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public class AddRequestState6 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Window window,ListOfCommands listOfCdes) {
        controller.setTour(tour);
        //TODO Update modify view
//        window.hideModifyMenu();
        listOfCdes.reset();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, String result, Window window,ListOfCommands listOfCdes) {
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
            listOfCdes.add(new AddRequestCommand(citymap, tour,controller.pickupToAdd, controller.deliveryToAdd));
            controller.setTour(tour);
            window.addMapRequest(tour.getRequests().get(tour.getNextRequestId()-1));
            //window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
            controller.setCurrentState(controller.tourComputedState);
            window.showWarningAlert("Modification", "Addition successfully completed", null);
        } else {
            window.showWarningAlert("Error","Wrong format, number must be a positive Integer" ,null);
            window.showInputAlert("Delivery Duration", "Please select the duration in second", "Delivery duration :");
        }
    }
}
