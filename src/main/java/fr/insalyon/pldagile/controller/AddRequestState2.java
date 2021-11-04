package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState2 implements State{


    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        controller.addRequest(id);
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour, Long intersectionId, Window window) {
        Intersection intersection = citymap.getIntersection(intersectionId);
        if(intersection == null){
            window.addStateFollow("Intersection number unknown, please try again");
        } else {
            window.addStateFollow("Intersection selected, you can now modify the duration in second or select the depot, a pickup or a delivery after which you want to place the delivery of your new request");
            controller.pickupToAdd = new Pair<Integer, Pickup>(controller.pickupToAdd.getKey(), new Pickup(intersection, 300));
            window.disableEventListener();
            controller.setCurrentState(controller.addRequestState3);
            window.addListPickup(controller.pickupToAdd.getKey(), controller.pickupToAdd.getValue(), tour.getNextRequestId());
            window.activeRowListener();
            window.activeRequestIntersectionsListener();
        }
    }
}
