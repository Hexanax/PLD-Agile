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
            window.showWarningAlert("How to add a request", "Intersection number unknown, please try again", null);
        } else {
            controller.pickupToAdd = new Pair<Integer, Pickup>(controller.pickupToAdd.getKey(), new Pickup(intersection, 300));
            //TODO Update
//            window.disableEventListener();
            controller.setCurrentState(controller.addRequestState3);
            window.showInputAlert("Pickup Duration", "Please select the duration in second", "Pickup duration :");
        }
    }
}
