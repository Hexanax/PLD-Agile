package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public class DeleteRequestState1 implements State{
    @Override
    public void deleteRequest(Controller controller,CityMap citymap, Tour tour, Tour modifyTour, Request request, Window window) {
        if(request != null){
            controller.requestToDelete = request;
            controller.setCurrentState(controller.deleteRequestState2);
            window.showValidationAlert("Delete request ?",
                    "Are you sure you want to delete the request no "+ (request.getId()+1)+" from intersection no "+request.getPickup().getIntersection().getId()+" to intersection no "+request.getDelivery().getIntersection().getId()+" ?",
                    null);
        } else {
            window.showWarningAlert("How to delete a request", "Request number unknown, please try again", null);
        }
    }

    @Override
    public void cancel(Controller controller, Tour tour, Tour modifyTour, Window window) {
        window.renderTour(tour.getIntersections());
        window.hideModifyMenu();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, Window window) {
        window.showWarningAlert("How to delete a request", null, "Please first select the request you want to delete");
    }
}
