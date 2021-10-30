package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;

public class DeleteRequestState2 implements State{
    @Override
    public void deleteRequest(Controller controller, CityMap citymap, Tour tour, Tour modifyTour, Request request, Window window) {
        TourBuilderV2 tourBuilder = new TourBuilderV2();
        modifyTour = tourBuilder.deleteRequest(citymap,modifyTour, request);
        controller.setModifyTour(modifyTour);
        window.renderTour(modifyTour.getIntersections());
        controller.setCurrentState(controller.modifyTourState);
        window.showWarningAlert("Modification", "Suppresion successfully completed", null);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, Window window) {
        this.deleteRequest(controller,citymap, tour, modifyTour, controller.requestToDelete, window);
    }

    @Override
    public void cancel(Controller controller, Tour tour,Tour modifyTour, Window window) {
        window.renderTour(modifyTour.getIntersections());
        window.disableEventListener();
        controller.setCurrentState(controller.modifyTourState);
    }
}
