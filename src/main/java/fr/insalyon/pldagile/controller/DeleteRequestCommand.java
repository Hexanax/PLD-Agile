package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

import java.util.Objects;

public class DeleteRequestCommand implements Command {

    private final Request deletedRequest;
    private final CityMap cityMap;
    private final PCLTour pclTour;
    private final TourBuilderV2 tourBuilder;
    private Pair<Integer, Pickup> pickup;
    private Pair<Integer, Delivery> delivery;

    public DeleteRequestCommand(CityMap citymap, PCLTour PCLtour, Request request) {
        this.cityMap = citymap;
        this.pclTour = PCLtour;
        this.deletedRequest = request;
        this.tourBuilder = new TourBuilderV2();
        int index = 0;
        for (Pair<Long, String> step : PCLtour.getTour().getSteps()) {
            if (Objects.equals(step.getKey(), request.getId())) {
                if (step.getValue().equals("pickup")) {
                    int value = index - 1;
                    pickup = new Pair<>(value, request.getPickup());
                } else {
                    int value = index - 1;
                    if (pickup.getKey() == (value - 1)) {
                        value = pickup.getKey();
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