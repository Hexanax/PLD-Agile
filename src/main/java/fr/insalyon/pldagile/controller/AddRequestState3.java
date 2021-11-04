package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import javafx.event.EventTarget;
import javafx.scene.input.KeyCode;

public class AddRequestState3 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Window window,ListOfCommands listOfCdes) {
        window.resetRequestListener();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        listOfCdes.reset();
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, String result, Window window,ListOfCommands listOfCdes) {


        if(validity(result)){
            int duration = Integer.parseInt(result);
            window.resetRequestListener();
            controller.pickupToAdd.getValue().setDuration(duration);
            controller.setCurrentState(controller.addRequestState4);
            window.disableEventListener();
            window.activeRequestIntersectionsListener();
            window.activeRowListener();
            window.addStateFollow("How to add a request : Select the depot, a pickup or a delivery after which you want to place the delivery of your new request");
        } else {
            window.addStateFollow("Error : Wrong format, number must be a positive Integer");
            window.addStateFollow("Intersection selected, you can now modify the duration in second or select the depot, a pickup or a delivery after which you want to place the delivery of your new request");
        }
    }

    @Override
    public void keystroke(Controller controller, KeyCode code, Window window, boolean isControlDown) {
        if(code==KeyCode.ENTER){
            controller.confirm(window.getRequestListener());
        }
        if(code==KeyCode.ESCAPE){
            controller.cancel();
        }
    }


    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        String result =  window.getRequestListener();
        if(validity(result)){
            int duration = Integer.parseInt(result);
            window.resetRequestListener();
            controller.pickupToAdd.getValue().setDuration(duration);
            controller.setCurrentState(controller.addRequestState4);
            controller.modifyClick(id,type,stepIndex);
        } else {
            window.addStateFollow("Error : Wrong format, number must be a positive Integer");
            window.addStateFollow("Intersection selected, you can now modify the duration in second or select the depot, a pickup or a delivery after which you want to place the delivery of your new request");
        }

    }

    private boolean validity(String result){
        boolean valid = false;
        int duration = 0;
        try {
            duration = Integer.parseInt(result);
            if(duration>=0){
                valid = true;
            }
        } catch (NumberFormatException e){
            valid = false;
        }

        return valid;

    }
}
