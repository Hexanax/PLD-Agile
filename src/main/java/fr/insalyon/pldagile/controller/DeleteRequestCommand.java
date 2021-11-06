package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

import java.util.Objects;

public class DeleteRequestCommand implements Command {

    private Request requestDeleted;
    private CityMap citymap;
    private PCLTour PCLtour;
    private TourBuilderV2 tourbuilder;
    private Pair<Integer, Pickup> pickup;
    private Pair<Integer, Delivery> delivery;

    public DeleteRequestCommand(CityMap citymap, PCLTour PCLtour, Request request){
        this.citymap = citymap;
        this.PCLtour = PCLtour;
        this.requestDeleted = request;
        this.tourbuilder = new TourBuilderV2();


        int index = 0;
        for(Pair<Long, String> step: PCLtour.getTour().getSteps()){

            if(Objects.equals(step.getKey(), request.getId())){
                if(step.getValue()=="pickup"){
                    int value = index -1;
                    pickup = new Pair<>(value, request.getPickup());
                } else {
                    int value = index-1;
                    if(pickup.getKey() == (value-1)){
                        value =  pickup.getKey();
                    }
                    delivery = new Pair<>(value, request.getDelivery());
                }
            }
            index++;
        }



    }


    @Override
    public void doCommand() {
        PCLtour.setTour(tourbuilder.deleteRequest(citymap,PCLtour.getTour(), requestDeleted));
    }

    @Override
    public void undoCommand() {
        PCLtour.setTour(tourbuilder.addRequest(citymap, PCLtour.getTour(), requestDeleted.getId()));
    }
}
