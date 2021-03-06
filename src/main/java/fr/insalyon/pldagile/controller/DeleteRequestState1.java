package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Request;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;

/**
 * DeleteRequestState1 is the state called when the user wants to select a request to delete
 */
public class DeleteRequestState1 implements State{
    @Override
    public void deleteRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, Long idRequest, Window window, ListOfCommands listOfCdes) {
        Request request = pcltour.getTour().getRequests().get(idRequest);
        if(request != null){
            try {
                listOfCdes.add(new DeleteRequestCommand(citymap,pclPlanningRequest, pcltour,request));
                window.addStateFollow("Suppresion successfully completed");
            } catch(Exception e) {
                window.addWarningStateFollow(e.getMessage());
            }
            controller.setCurrentState(controller.tourComputedState);
        } else {
            window.addWarningStateFollow("Request number unknown, please try again");
        }

        window.mainView();
    }

    @Override
    public void modifyClick(Controller controller, PlanningRequest planningRequest, Tour tour, Long idRequest, String type, int stepIndex, Window window) {
        if(idRequest != -1){
            controller.deleteRequest(idRequest);
        }
    }
}
