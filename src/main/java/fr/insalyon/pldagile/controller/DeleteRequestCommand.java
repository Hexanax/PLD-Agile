package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.tsp.ExceptionCityMap;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

import java.util.Objects;

public class DeleteRequestCommand implements Command {

    private Request deletedRequest;
    private final CityMap cityMap;
    private final PCLTour pclTour;
    private final PCLPlanningRequest pclPlanningRequest;
    private final TourBuilderV2 tourBuilder;
    private int indexBeforePickup;
    private int indexBeforeDelivery;

    public DeleteRequestCommand(CityMap citymap, PCLPlanningRequest pCLPlanningRequest, PCLTour PCLtour, Request request) {
        this.pclPlanningRequest = pCLPlanningRequest;
        this.cityMap = citymap;
        this.pclTour = PCLtour;
        this.deletedRequest = request;
        this.tourBuilder = new TourBuilderV2();
        int index = 0;
        for (Pair<Long, String> step : PCLtour.getTour().getSteps()) {
            if (Objects.equals(step.getKey(), request.getId())) {
                if (step.getValue().equals("pickup")) {
                    indexBeforePickup = index - 1;
                } else {
                    indexBeforeDelivery = index - 1;
                }
            }
            index++;
        }
    }

    @Override
    public void doCommand() throws ExceptionCityMap {
        //System.out.println("Do command for the delete request");
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        planningRequest.deleteRequest(deletedRequest.getId());
        pclPlanningRequest.setPlanningRequest(planningRequest);
        pclTour.setTour(tourBuilder.deleteRequest(cityMap, pclTour.getTour(), deletedRequest));
    }

    @Override
    public void undoCommand() throws ExceptionCityMap {
        //System.out.println("UNDO CALLED");
        System.out.println("undo command for the delete request");
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        planningRequest.add(deletedRequest, false);

        Tour tour = new Tour(pclTour.getTour());
        tour.addRequest(deletedRequest);
        tour.addStep(indexBeforePickup, new Pair<>(deletedRequest.getId(), "pickup"));
        tour.addStep(indexBeforeDelivery, new Pair<>(deletedRequest.getId(), "delivery"));

        pclPlanningRequest.setPlanningRequest(planningRequest);
        pclTour.setTour(tourBuilder.addRequest(cityMap, tour, deletedRequest.getId()));
    }

    @Override
    public void editRequestDuration(int pickupDuration, int deliveryDuration) throws ExceptionCityMap {
        deletedRequest.getPickup().setDuration(pickupDuration);
        deletedRequest.getDelivery().setDuration(deliveryDuration);

        Tour modifyTourDuration = new Tour(pclTour.getTour());

        modifyTourDuration.reset();
        modifyTourDuration = tourBuilder.computeTour(cityMap, modifyTourDuration, modifyTourDuration.getIntersections());
        pclTour.setTour(modifyTourDuration);

    }

}