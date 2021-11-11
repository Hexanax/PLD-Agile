package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;

public interface TourBuilder {

    public Tour buildTour(PlanningRequest planningRequest, CityMap cityMap);

}