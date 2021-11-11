package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ButtonListener {

    private static Controller controller;

    public static void setController(Controller newController) {
        controller = newController;
    }

    public static void actionPerformed(ActionEvent event) {
        switch (((Button) event.getTarget()).getText()) {
            case ImportView.LOAD_MAP:
                controller.loadMap();
                break;
            case ImportView.LOAD_REQUESTS:
                controller.loadRequests();
                break;
            case ImportView.COMPUTE_TOUR:
                controller.computeTour();
                break;
            case ImportView.GENERATE_ROADMAP:
                controller.generateRoadMap();
                break;
            case RequestView.DELETE_REQUEST:
                controller.deleteRequest(null);
                break;
            case RequestView.ADD_REQUEST:
                controller.addRequest();
                break;
            case RequestView.REDO:
                controller.redo();
                break;
            case RequestView.UNDO:
                controller.undo();
                break;
        }

    }
}
