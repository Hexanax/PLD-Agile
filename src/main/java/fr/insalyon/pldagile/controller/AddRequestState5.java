package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState5 implements State{

    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        controller.addRequest(id);
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour,  Long intersectionId, Window window) {
        Intersection intersection = citymap.getIntersection(intersectionId);
        if(intersection == null){
            window.showWarningAlert("How to add a request", "Intersection number unknown, please try again", null);
        } else if(intersectionId == controller.pickupToAdd.getValue().getIntersection().getId()){
            window.showWarningAlert("How to add a request", "You can't add a request with the same address for pickup and delivery", "please try again");
        } else {
            controller.deliveryToAdd = new Pair<Integer, Delivery>(controller.deliveryToAdd.getKey(), new Delivery(intersection, 300));
            //TODO Update
//            window.disableEventListener();
            controller.setCurrentState(controller.addRequestState6);
            window.showInputAlert("Delivery Duration", "Please select the duration in second", "Delivery duration :");
        }
    }
}
