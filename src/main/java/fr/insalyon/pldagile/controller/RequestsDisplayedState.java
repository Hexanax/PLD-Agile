package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.tsp.ExceptionCityMap;
import fr.insalyon.pldagile.services.TourBuilderV2;
import fr.insalyon.pldagile.view.Window;

/**
 *  RequestDisplayedState is the state when map & requests
 *  are loaded in the system
 */
public class RequestsDisplayedState implements State{
    @Override
    public void loadMap(Controller controller, Window window, ListOfCommands l) {
        controller.setCurrentState(controller.mapOverwrite2State);
        window.showValidationAlert("Load a new map",
                "Are you sure you want to load a new map? ",
                "This will remove the requests already loaded");
    }

    @Override
    public void loadRequests(Controller controller, CityMap cityMap, Window window, ListOfCommands l) {
        controller.setCurrentState(controller.requestsOverwrite1State);
        window.showValidationAlert("Load new requests",
                "Are you sure you want to load new requests ? ",
                "This will remove the requests already loaded");
    }

    @Override
    public void computeTour(Controller controller, CityMap cityMap, PlanningRequest planningRequest, Window window) {
        window.addStateFollow("Tour calculation ...");
        TourBuilderV2 tourBuilderV2 = new TourBuilderV2();
        try {
            Tour tour = tourBuilderV2.buildTour(planningRequest, cityMap, () -> {
                if(window.continueTourCompute()){
                    try {
                        tourBuilderV2.getSimulatedAnnealing().runSimulatedAnnealing(
                                tourBuilderV2.getSimulatedAnnealing().getTemperature(),
                                tourBuilderV2.getSimulatedAnnealing().getNumberOfIterations(),
                                tourBuilderV2.getSimulatedAnnealing().getCoolingRate(),
                                false
                                );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };

            });
            controller.setTour(tour);
            window.addStateFollow("Tour computed");
            controller.setCurrentState(controller.tourComputedState);
        } catch (ExceptionCityMap | InterruptedException e) {
            window.addWarningStateFollow(e.getMessage());
        }

    }
}
