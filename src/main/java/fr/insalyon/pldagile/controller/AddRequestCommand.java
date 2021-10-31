package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.TourBuilderV2;
import javafx.util.Pair;

public class AddRequestCommand implements Command {
    private Tour tour;
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
    }


    @Override
    public void doCommand() {
        tour = tourBuilder.addRequest(cityMap, tour, pickup, delivery);
        requestAdded = tour.getRequests().get(tour.getNextRequestId()-1);
    }

    @Override
    public void undoCommand() {
        tour = tourBuilder.deleteRequest(cityMap, tour, requestAdded);
        tour.setNextRequestId(tour.getNextRequestId()-1);
    }
}
