package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
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
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
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


    @Override
    public void loadMap(Controller controller, Window window) {
        controller.confirm();
        controller.loadMap();
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window) {
        controller.confirm();
        controller.loadRequests();
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window) {
        controller.confirm();
        controller.loadRequests();
    }

    @Override
    public void deleteRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, Long idRequest, Window window, ListOfCommands listOfCdes) {
        controller.confirm();
        controller.deleteRequest(null);
    }

    @Override
    public void undo(Controller controller, ListOfCommands l, Window w) {
        controller.confirm();
        controller.undo();
    }

    @Override
    public void redo(Controller controller, ListOfCommands l, Window w) {
        controller.confirm();
        controller.redo();
    }

    @Override
    public void generateRoadMap(Controller controller, Tour tour, Window window) {
        controller.confirm();
        controller.generateRoadMap();
    }
}
