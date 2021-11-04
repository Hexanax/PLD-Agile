package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState4 implements State{


    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        if(stepIndex==-2) { stepIndex = controller.pickupToAdd.getKey(); }
        if(type=="Depot" && stepIndex!=0)
        {
            window.addStateFollow("You can't add a request after the arrival of the tour");
        } else if(stepIndex < controller.pickupToAdd.getKey()) {
            window.addStateFollow("You can't add a request with delivery before pickups");
        }
        else {


            controller.deliveryToAdd = new Pair<Integer, Delivery>(stepIndex, null);
            window.addStateFollow("Please select on the map the intersection of your delivery");
            window.disableEventListener();
            window.activeMapIntersectionsListener();
            window.disableMainListener();
            controller.setCurrentState(controller.addRequestState5);
        }

    }
}
