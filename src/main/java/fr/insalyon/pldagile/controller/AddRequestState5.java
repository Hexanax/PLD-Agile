package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class AddRequestState5 implements State{

    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        controller.addRequest(id);
        window.activeMainListener();
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour,  Long intersectionId, Window window) {
        Intersection intersection = citymap.getIntersection(intersectionId);
        if(intersection == null){
            window.addStateFollow( "Intersection number unknown, please try again");
        } else if(intersectionId == controller.pickupToAdd.getValue().getIntersection().getId()){
            window.addStateFollow("You can't add a request with the same address for pickup and delivery, try again");
        } else {
            controller.deliveryToAdd = new Pair<Integer, Delivery>(controller.deliveryToAdd.getKey(), new Delivery(intersection, 300));
            window.addStateFollow("Intersection selected, you can now modify the duration in second or click where you want out of the requests list to valid the creation");
            window.addListDelivery(controller.deliveryToAdd.getKey(), controller.deliveryToAdd.getValue(), tour.getNextRequestId());
            window.disableEventListener();
        }
    }

    @Override
    public void leftClick(Controller controller, MouseEvent event, Window window) {
        if(controller.deliveryToAdd.getValue() != null){
            controller.setCurrentState(controller.addRequestState6);
        } else {
            controller.setCurrentState(controller.addRequestState5);
        }
    }
}
