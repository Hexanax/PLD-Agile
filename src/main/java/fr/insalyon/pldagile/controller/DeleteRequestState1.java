package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;

public class DeleteRequestState1 implements State{
    @Override
    public void deleteRequest(Controller controller,CityMap citymap, Tour tour,  Request request, Window window,ListOfCommands listOfCdes) {
        if(request != null){
            listOfCdes.add(new DeleteRequestCommand(citymap,tour,request));
            window.renderTour(tour);
            window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
            controller.setCurrentState(controller.tourComputedState);
            window.addStateFollow("Suppresion successfully completed");
        } else {
            window.addStateFollow("Request number unknown, please try again");
        }
    }

    @Override
    public void modifyClick(Controller controller, Long idRequest, String type, int stepIndex, Window window) {
        if(idRequest != -1){
            controller.deleteRequest(idRequest);
        }
    }
}
