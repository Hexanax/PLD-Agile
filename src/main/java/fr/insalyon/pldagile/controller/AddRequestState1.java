package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Pickup;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

public class AddRequestState1 implements State {

    @Override
    public void modifyClick(Controller controller, Long id, String type, int stepIndex, Window window) {
        if(type=="Depot" && stepIndex!=0)
        {
            window.showWarningAlert("How to add a request", "You can't add a request after the arrival of the tour", null);
        } else {
            controller.pickupToAdd = new Pair<Integer, Pickup>(stepIndex, null);
            window.showWarningAlert("How to add a request", "Please select on the map the intersection of your pickup", null);
            //TODO Update
//            window.disableEventListener();
            window.getCityMapView().activeMapIntersectionsListener();
            controller.setCurrentState(controller.addRequestState2);
        }

    }
}
