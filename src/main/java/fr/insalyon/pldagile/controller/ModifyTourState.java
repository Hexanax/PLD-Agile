package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;

public class ModifyTourState implements State{
    @Override
    public void cancel(Controller controller, Window window) {
        //TODO delete modification
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Window window) {
        //TODO save modification
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }
}
