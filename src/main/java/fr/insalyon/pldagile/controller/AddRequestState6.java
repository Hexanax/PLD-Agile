package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import javafx.event.EventTarget;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;


public class AddRequestState6 implements State{
    @Override
    public void cancel(Controller controller, Tour tour, Window window,ListOfCommands listOfCdes) {
        window.resetRequestListener();
        window.renderTour(tour);
        window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
        window.hideModifyMenu();
        window.addStateFollow("Add request cancel");
        listOfCdes.reset();
        controller.setCurrentState(controller.tourComputedState);
    }

    @Override
    public void confirm(Controller controller, CityMap citymap, PlanningRequest planningRequest, Tour tour, String result, Window window,ListOfCommands listOfCdes) {
        int duration = 0;
        boolean valid = false;
        try {
            duration = Integer.parseInt(result);
            if(duration>=0){
                valid = true;
            }
        } catch (NumberFormatException e){
            valid = false;
        }

        if(valid){
            controller.pickupToAdd.getValue().setDuration(Integer.parseInt(result));
            listOfCdes.add(new AddRequestCommand(citymap, tour,controller.pickupToAdd, controller.deliveryToAdd));
            window.resetRequestListener();
            window.renderTour(tour);
            window.addMapRequest(tour.getRequests().get(tour.getNextRequestId()-1));
            window.orderListRequests(tour.getSteps(), tour.getRequests(), tour.getDepot());
            controller.setCurrentState(controller.tourComputedState);
            window.addStateFollow( "Addition successfully completed");
        } else {
            window.addStateFollow("Wrong format, number must be a positive Integer" );
            window.addStateFollow("Intersection selected, you can now modify the duration in second or click where you want out of the requests list to valid the creation");
        }
    }

    @Override
    public void keystroke(Controller controller, KeyCode code, Window window, boolean isControlDown) {
        System.out.println(code);
        if(code==KeyCode.ENTER){
            controller.confirm(window.getRequestListener());
        }
        if(code==KeyCode.ESCAPE){
            controller.cancel();
        }
    }

    @Override
    public void leftClick(Controller controller, MouseEvent event, Window window) {
        controller.confirm(window.getRequestListener());


        String value = "";
        if(event.getTarget().getClass().getSimpleName().equals("Button")){

            value = ((javafx.scene.control.Button) event.getTarget()).getText();
        }

        if(event.getSource().getClass().getSimpleName().equals("Button")){
            value = ((javafx.scene.control.Button) event.getSource()).getText();
        }

        switch (value){
            case "Import map" :
                controller.loadMap(); break;
            case "Import Requests" :
                controller.loadRequests(); break;
            case "Add Request" :
                controller.addRequest(null); break;
            case "Delete Request":
                controller.deleteRequest(null); break;
            case "Redo":
                controller.redo(); break;
            case "Undo":
                controller.undo(); break;
            case "Generate the Road Map":
                controller.generateRoadMap(); break;
        }
    }


    private boolean validity(String result){
        boolean valid = false;
        int duration = 0;
        try {
            duration = Integer.parseInt(result);
            if(duration>=0){
                valid = true;
            }
        } catch (NumberFormatException e){
            valid = false;
        }

        return valid;

    }
}
