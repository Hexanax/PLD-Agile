package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;

public class AddRequestState7 implements State {
    @Override
    public void addRequest(Controller controller, CityMap citymap, Tour tour, Tour modifyTour, Long intersectionID, Window window) {
        TourBuilderV2 tourBuilder = new TourBuilderV2();
        System.out.println("pickupToAdd:" + controller.pickupToAdd);
        System.out.println("deliveryToAdd:" + controller.deliveryToAdd);
        modifyTour = tourBuilder.addRequest(citymap, tour, controller.pickupToAdd, controller.deliveryToAdd);
        controller.setModifyTour(modifyTour);
        window.renderTour(modifyTour.getIntersections());
        window.addMapRequest(modifyTour.getRequests().get(modifyTour.getNextRequestId()-1));
        window.orderListRequests(modifyTour.getSteps(), modifyTour.getRequests(), tour.getDepot());
        controller.setCurrentState(controller.modifyTourState);
        window.showWarningAlert("Modification", "Addition successfully completed", null);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Tour modifyTour, String result, Window window) {
        this.addRequest(controller, citymap, tour, modifyTour, null, window);
    }

    @Override
    public void cancel(Controller controller, Tour tour,Tour modifyTour, Window window) {
        window.renderTour(modifyTour.getIntersections());
        window.disableEventListener();
        controller.setCurrentState(controller.modifyTourState);
    }
}
