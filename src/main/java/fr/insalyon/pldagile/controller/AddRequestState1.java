package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState1 implements State {


    @Override
    public void intersectionClick(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Long intersectionID, Window window) {
        Intersection intersection = cityMap.getIntersection(intersectionID);
        if(intersection == null){
            window.addStateFollow("Intersection number unknown, please try again");
        } else {
            //TODO display the pickup on the map
            Request request = new Request(new Pickup(intersection, 300), null);
            planningRequest.add(request);
            PlanningRequest modify = new PlanningRequest(planningRequest);
            controller.setPlanningRequest(modify);
            controller.setCurrentState(controller.addRequestState2);
            window.addStateFollow("Pickup intersection selected, Now left click on the intersection where the delivery will take place or right click to cancel");
        }
    }

    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        //TODO display the tour
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }
}
