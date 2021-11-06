package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
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
    public void doCommand() {
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        int index = 0;
        for (Request request : planningRequest.getRequests()) {
            if (Objects.equals(request.getId(), deletedRequest.getId())) {
                planningRequest.getRequests().remove(index);
                break;
            }
            index++;
        }
        pclPlanningRequest.setPlanningRequest(planningRequest);
        pclTour.setTour(tourBuilder.deleteRequest(cityMap, pclTour.getTour(), deletedRequest));
    }

    @Override
    public void undoCommand() {
        System.out.println("UNDO CALLED");
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        planningRequest.add(deletedRequest, false);

        Tour tour = new Tour(pclTour.getTour());
        tour.addRequest(deletedRequest);
        tour.addStep(indexBeforePickup, new Pair<>(deletedRequest.getId(), "pickup"));
        tour.addStep(indexBeforeDelivery, new Pair<>(deletedRequest.getId(), "delivery"));

        pclPlanningRequest.setPlanningRequest(planningRequest);
        pclTour.setTour(tourBuilder.addRequest(cityMap, tour, deletedRequest.getId()));
    }

}