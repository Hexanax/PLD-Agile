package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.observer.PCLPlanningRequest;
import fr.insalyon.pldagile.observer.PCLTour;
import fr.insalyon.pldagile.view.Window;
import javafx.util.Pair;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class AddRequestState4 implements State {

    @Override
    public void cancel(Controller controller, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        System.out.println("Cancelled called " + System.currentTimeMillis());
        long idRequestDelete = planningRequest.getLastRequest().getId();
        planningRequest.deleteLastRequest();
        PlanningRequest modify = new PlanningRequest(planningRequest);
        controller.setPlanningRequest(modify);

        Tour modifyTour = new Tour(tour);
        modifyTour.deleteRequest(idRequestDelete);
        controller.setTour(modifyTour);

        window.mainView();
        window.addStateFollow("Add request cancel");
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void modifyClick(Controller controller, PlanningRequest planningRequest, Tour tour, Long id, String type, int stepIndex, Window window) {
        validClick = false;
        if (Objects.equals(type, "Depot") && stepIndex != 0) {
            window.addWarningStateFollow("You can't add a request after the arrival of the tour");
        } else {
            Request request = planningRequest.getLastRequest();
            if (Objects.equals(id, request.getId()) && Objects.equals(type, "delivery")) {
                window.addWarningStateFollow("You can't make the request after the request itself");
            } else {
                boolean bufferClicks = false;
                if (stepIndex == -1) {
                    bufferClicks = true;
                    if (type.equals("depot")) {
                        stepIndex = 0;
                    } else {
                        Pair<Long, String> stepToFound = new Pair<Long, String>(id, type);
                        stepIndex = tour.getSteps().indexOf(stepToFound);
                    }
                }
                if (stepIndex < tour.getSteps().indexOf(new Pair<>(request.getId(), "pickup"))) {
                    window.addWarningStateFollow("You can't make the delivery before the pickup");
                } else {
                    tour.addStep(stepIndex, new Pair<>(request.getId(), "delivery"));
                    controller.addRequest();
                    if (bufferClicks) {
                        validClick = true;
                    } else {
                        window.unHighlightAddress(planningRequest.getLastRequest().getId());
                        controller.setCurrentState(controller.addRequestState5);
                    }
                }
            }
        }
    }

    @Override
    public void addRequest(Controller controller, CityMap citymap, PCLPlanningRequest pclPlanningRequest, PCLTour pcltour, ListOfCommands l, Window window) {
        try {
            l.add(new AddRequestCommand(citymap, pclPlanningRequest, pcltour));
            window.mainView();
            window.addStateFollow("Delivery previous address selected, you can now modify the duration in second of the pickup and the delivery or click where you want out of the requests list to valid the creation");
            window.makeLastRequestAddedEditable(true, pclPlanningRequest.getPlanningRequest().getLastRequest().getId());
        } catch (Exception e) {
            System.out.println("Exception caught, we have to the cancel the add request");
            e.printStackTrace();
            window.addWarningStateFollow(e.getMessage());
            controller.cancel();
        }
    }

    private boolean validClick;

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, Window window, ListOfCommands l) {
        if (validClick) {
            validClick = false;
            window.unHighlightAddress(planningRequest.getLastRequest().getId());
            controller.setCurrentState(controller.addRequestState5);
        }
    }
}
