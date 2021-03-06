package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

import java.util.Objects;

/**
 * AddRequestState3 is the third state called when the user wants to add a request
 * In this state to add a request the user must select the previous address in the tour (step) before the pickup added
 */
public class AddRequestState3 implements State{
    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        window.addStateFollow("Add request cancel");
        planningRequest.deleteLastRequest();
        PlanningRequest modify = new PlanningRequest(planningRequest);
        controller.setPlanningRequest(modify);
        controller.setCurrentState(controller.tourComputedState);
        window.mainView();
    }

    @Override
    public void modifyClick(Controller controller,PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window) {
        //The use can call the modifyClick with 2 way

        //First he can select an address in the list
        if(Objects.equals(type, "Depot") && stepIndex!=0)
        {
            window.addWarningStateFollow( "You can't add a request after the arrival of the tour");
        } else {
            Request request = planningRequest.getLastRequest();
            if(Objects.equals(id, request.getId())){
                window.addWarningStateFollow( "You can't make the request after the request itself");
            } else {
                //Second he can directly click at the address in the map
                if(stepIndex==-1){
                    if(Objects.equals(type, "depot")){
                        stepIndex =0;
                    } else {
                        Pair<Long, String> stepToFound = new Pair<>(id, type);
                        stepIndex = tour.getSteps().indexOf(stepToFound);
                    }
                }
                tour.addRequest(planningRequest.getLastRequest());
                tour.addStep(stepIndex,new Pair<>(request.getId(), "pickup"));
                Tour modify = new Tour(tour);
                controller.setTour(modify);
                window.addStateFollow("You have successfully selected the address before your new pickup. Left click on the map pin to visit before your new delivery");

                window.hideTour();
                window.highlightAddress(planningRequest.getLastRequest().getId(), "delivery");
                controller.setCurrentState(controller.addRequestState4);
            }
        }
    }
}
