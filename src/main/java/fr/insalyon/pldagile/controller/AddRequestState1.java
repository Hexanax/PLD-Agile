package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState1 implements State {


    @Override
    public void intersectionClick(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Long intersectionID, Window window) {
        TourBuilderV2 tourBuilder = new TourBuilderV2();
        if(tourBuilder.deadEndIntersection(cityMap, intersectionID)){
            window.addWarningStateFollow("The intersection is a dead end Intersection with the current loaded city map, you can't use it");
            window.addWarningStateFollow("Try again");
        } else {
            Intersection intersection = cityMap.getIntersection(intersectionID);
            if(intersection == null){
                window.addStateFollow("Intersection number unknown, please try again");
            } else {
                Request request = new Request(new Pickup(intersection, 300), null);
                planningRequest.add(request);
                PlanningRequest modify = new PlanningRequest(planningRequest);
                controller.setPlanningRequest(modify);
                controller.setCurrentState(controller.addRequestState2);
                window.addStateFollow("Pickup intersection selected, Now left click on the intersection where the delivery will take place or right click to cancel");
            }
        }

    }

    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        window.addStateFollow("Add request cancel");
        window.mainView();
        controller.setCurrentState(controller.tourComputedState);
    }
}
