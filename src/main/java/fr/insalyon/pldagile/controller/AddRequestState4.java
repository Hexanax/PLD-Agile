package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

import java.util.Objects;

public class AddRequestState4 implements State{


    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        //TODO display the tour
        long idRequestDelete = planningRequest.getLastRequest().getId();
        planningRequest.deleteLastRequest();
        PlanningRequest modify = new PlanningRequest(planningRequest);
        controller.setPlanningRequest(modify);

        tour.deleteRequest(idRequestDelete);
        Tour modifyTour = new Tour(tour);
        controller.setTour(modifyTour);

        window.showCityMap();
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void modifyClick(Controller controller,PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window) {
        if(type=="Depot" && stepIndex!=0)
        {
            window.addWarningStateFollow("You can't add a request after the arrival of the tour");
        } else if(stepIndex < tour.getSteps().indexOf(planningRequest.getLastRequest().getId())) {
            window.addWarningStateFollow("You can't add a request with delivery before pickups");
        }
        else {

            Request request = planningRequest.getLastRequest();
            tour.addStep(stepIndex,new Pair<>(request.getId(), "delivery"));
            Tour modify = new Tour(tour);
            controller.setTour(modify);


            controller.addRequest(null);


        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window) {


        l.add(new AddRequestCommand(citymap, pclPlanningRequest, pcltour));

        controller.setCurrentState(controller.addRequestState5);
        window.showCityMap();
        window.addStateFollow("Delivery previous address selected, you can now modify the duration in second of the pickup and the delivery or click where you want out of the requests list to valid the creation");

    }
}
