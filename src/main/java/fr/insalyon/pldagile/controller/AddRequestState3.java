package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

import java.util.Objects;

public class AddRequestState3 implements State{
    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        //TODO display the tour
        window.addStateFollow("Add request cancel");
        planningRequest.deleteLastRequest();
        PlanningRequest modify = new PlanningRequest(planningRequest);
        controller.setPlanningRequest(modify);
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void modifyClick(Controller controller,PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window) {
        if(Objects.equals(type, "Depot") && stepIndex!=0)
        {
            window.addWarningStateFollow( "You can't add a request after the arrival of the tour");

        } else {
            Request request = planningRequest.getLastRequest();
            tour.addRequest(planningRequest.getLastRequest());
            tour.addStep(stepIndex,new Pair<>(request.getId(), "pickup"));
            Tour modify = new Tour(tour);
            controller.setTour(modify);

            window.addStateFollow("Pickup previous address selected, Now left click on the depot, pickup or delivery visiting before the pickup");
            controller.setCurrentState(controller.addRequestState4);
        }
    }
}
