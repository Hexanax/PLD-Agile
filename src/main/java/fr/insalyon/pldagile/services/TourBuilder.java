package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;

/**
 * Interface defining the main function of TourBuilder classes, which is building a tour from a PlanningRequest
 * and a CityMap.
 */
public interface TourBuilder {

    /**
     *
     * @param planningRequest planning of requests loaded in our application
     * @param cityMap city map loaded in our application
     * @return a Tour object that represents the list of ordered requests to visit computed from a planning request and the cityMap
     */
    public Tour buildTour(PlanningRequest planningRequest, CityMap cityMap);

}