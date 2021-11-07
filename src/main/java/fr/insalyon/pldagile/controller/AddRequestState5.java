package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState5 implements State{

    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        l.cancel();
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, String result, Window window, ListOfCommands l) {
        //TODO read value
        String[] values = window.getEditableRequestDuration();
        if(values == null){
            window.addWarningStateFollow("Error : duration can't be null");
        } else {
            if(validity(values[0]) && validity(values[1])){
                int pickupDuration = Integer.parseInt(values[0]);
                int deliveryDuration = Integer.parseInt(values[0]);
                l.getLastCommand().editRequestDuration(pickupDuration, deliveryDuration);
                window.makeLastRequestAddedEditable(false, planningRequest.getLastRequest().getId());
                window.addStateFollow("Request suscesfully added");
                controller.setCurrentState(controller.tourComputedState);
            } else {
                window.addStateFollow("Wrong format, number must be a positive Integer, try again" );
            }

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
