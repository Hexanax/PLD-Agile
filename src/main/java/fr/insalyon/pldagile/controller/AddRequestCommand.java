package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.tsp.ExceptionCityMap;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

import java.util.Objects;

public class AddRequestCommand implements Command {
    /*private Tour tour;
    private CityMap cityMap;
    private Pair<Integer, Pickup> pickup;
    private Pair<Integer, Delivery> delivery;
    private TourBuilderV2 tourBuilder;
    private Request requestAdded;

    public AddRequestCommand(CityMap citymap, Tour tour, Pair<Integer, Pickup> pickup, Pair<Integer, Delivery> delivery){
        this.tour = tour;
        this.pickup = pickup;
        this.delivery = delivery;
        this.tourBuilder = new TourBuilderV2();
        this.cityMap = citymap;
    }*/

    private CityMap cityMap;
    private PCLPlanningRequest pclPlanningRequest;
    private PCLTour pcltour;
    private Request request;
    private TourBuilderV2 tourBuilder;

    private int indexBeforePickup;
    private int indexBeforeDelivery;

    private boolean delete;

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


    @Override
    public void doCommand() throws ExceptionCityMap {
        //System.out.println("Do command for the add request");
        if(delete){
            PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
            planningRequest.add(request, false);
            pclPlanningRequest.setPlanningRequest(planningRequest);

            Tour tour = new Tour(pcltour.getTour());
            tour.addRequest(request);
            tour.addStep(indexBeforePickup, new Pair<>(request.getId(),"pickup"));
            tour.addStep(indexBeforeDelivery, new Pair<>(request.getId(),"delivery"));
            pcltour.setTour(tour);

        }
        delete = false;

        pcltour.setTour(tourBuilder.addRequest(cityMap, pcltour.getTour(), request.getId()));
    }

    @Override
    public void undoCommand() throws ExceptionCityMap {
        //System.out.println("Undo command for the add request");
        delete = true;
        PlanningRequest planningRequest = new PlanningRequest(pclPlanningRequest.getPlanningRequest());
        planningRequest.deleteRequest(request.getId());
        pclPlanningRequest.setPlanningRequest(planningRequest);
        pcltour.setTour(tourBuilder.deleteRequest(cityMap, pcltour.getTour(), request));
    }

    @Override
    public void editRequestDuration(int pickupDuration, int deliveryDuration) throws ExceptionCityMap {
        request.getPickup().setDuration(pickupDuration);
        request.getDelivery().setDuration(deliveryDuration);

        Tour modifyTourDuration = new Tour(pcltour.getTour());

        modifyTourDuration.reset();
        modifyTourDuration = tourBuilder.computeTour(cityMap, modifyTourDuration, modifyTourDuration.getIntersections());
        pcltour.setTour(modifyTourDuration);

    }
}
