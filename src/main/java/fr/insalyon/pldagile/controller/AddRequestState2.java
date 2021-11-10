package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;

/**
 * AddRequestState2 is the second state called when the user wants to add a request
 * In this state to add a request the user must select the intersection of the delivery
 */
public class AddRequestState2 implements State{

    @Override
    public void intersectionClick(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Long intersectionID, Window window) {
        TourBuilderV2 tourBuilder = new TourBuilderV2();
        if(tourBuilder.deadEndIntersection(cityMap, intersectionID)){
            window.addWarningStateFollow("The intersection is a dead end Intersection with the current loaded city map, you can't use it \n Try again");
        } else {
            Intersection intersection = cityMap.getIntersection(intersectionID);
            if(intersection == null){
                window.addWarningStateFollow("Intersection number unknown, please try again");
            } else if(intersectionID == planningRequest.getLastRequest().getPickup().getIntersection().getId()){
                window.addWarningStateFollow("You can't add a request with the same address for pickup and delivery, try again");
            }else {
                planningRequest.getLastRequest().setDelivery(new Delivery(intersection, 300));
                PlanningRequest modify = new PlanningRequest(planningRequest);
                controller.setPlanningRequest(modify);
                controller.setCurrentState(controller.addRequestState3);
                window.addStateFollow("Delivery intersection selected, Now left click on the depot, pickup or delivery visiting before the pickup");
                window.hideCityMap();
                window.renderOrderedList();
                window.highlightAddress(planningRequest.getLastRequest().getId(), "pickup");
            }
        }
    }

    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        window.mainView();
        planningRequest.deleteLastRequest();
        PlanningRequest modify = new PlanningRequest(planningRequest);
        controller.setPlanningRequest(modify);
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }
}
