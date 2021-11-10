package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.tsp.ExceptionCityMap;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

import java.util.Objects;

/**
 * This class allows executing or cancel the command to add a request
 */
public class AddRequestCommand implements Command {

    private CityMap cityMap;
    private PCLPlanningRequest pclPlanningRequest;
    private PCLTour pcltour;
    private Request request;
    private TourBuilderV2 tourBuilder;

    private int indexBeforePickup;
    private int indexBeforeDelivery;

    private boolean delete;

    /**
     * Allows to prepare the undo/redo interface to add a request
     * @param citymap the city map
     * @param pclPlanningRequest the observable planning request
     * @param pcltour the observable tour
     */
    public AddRequestCommand(CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour) {
        this.request = pclPlanningRequest.getPlanningRequest().getLastRequest();
        this.cityMap = citymap;
        this.pclPlanningRequest = pclPlanningRequest;
        this.pcltour = pcltour;
        this.tourBuilder = new TourBuilderV2();

        int index =0;
        for(Pair<Long,String> step : pcltour.getTour().getSteps()){
            if(Objects.equals(step.getKey(), request.getId())){
                if(step.getValue()=="pickup"){
                    indexBeforePickup = index-1;
                } else {
                    indexBeforeDelivery = index -1;
                }
            }
            index++;
        }

        delete = false;


    }


    /**
     * Execute the add request command
     * @throws ExceptionCityMap
     */
    @Override
    public void doCommand() throws ExceptionCityMap {
        //In case the order has been cancelled once, we have to rebuild the planning request and the tour correctly before updated it
        if(delete){
            PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
            planningRequest.add(request, false);
            pclPlanningRequest.setPlanningRequest(planningRequest);

            Tour tour = new Tour(pcltour.getTour());
            tour.addRequest(request);
            //Insert in the good position the pickup and the delivery of the request
            tour.addStep(indexBeforePickup, new Pair<>(request.getId(),"pickup"));
            tour.addStep(indexBeforeDelivery, new Pair<>(request.getId(),"delivery"));
            pcltour.setTour(tour);

        }
        delete = false;

        //Tour update
        pcltour.setTour(tourBuilder.addRequest(cityMap, pcltour.getTour(), request.getId()));
    }

    /**
     * Cancel the add request command
     * @throws ExceptionCityMap
     */
    @Override
    public void undoCommand() throws ExceptionCityMap {
        delete = true;
        //Tour and planning request update
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        planningRequest.deleteRequest(request.getId());
        pclPlanningRequest.setPlanningRequest(planningRequest);
        pcltour.setTour(tourBuilder.deleteRequest(cityMap, pcltour.getTour(), request));
    }

    /**
     * Allows to modify the duration of the pickup and the delivery of the
     * @param pickupDuration pickup duration edited
     * @param deliveryDuration delivery duration edited
     * @throws ExceptionCityMap
     */
    @Override
    public void editRequestDuration(int pickupDuration, int deliveryDuration) throws ExceptionCityMap {
        request.getPickup().setDuration(pickupDuration);
        request.getDelivery().setDuration(deliveryDuration);

        Tour modifyTourDuration = new Tour(pcltour.getTour());

        modifyTourDuration.reset();
        //Recompute the duration of the tour
        modifyTourDuration = tourBuilder.computeTour(cityMap, modifyTourDuration, modifyTourDuration.getIntersections());
        pcltour.setTour(modifyTourDuration);

    }
}
