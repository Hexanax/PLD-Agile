package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * Static button listener, listening to the actions related to the button clicks, depending on the target
 */
public class ButtonListener {

    private static Controller controller;

    public static void setController(Controller newController) {
        controller = newController;
    }

    /**
     * Performed action handler and dispatcher to the controller
     *
     * @see ActionEvent
     */
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
            case ImportView.SLOW_COMPUTE_TOUR:
                controller.slowComputeTour();
                break;
            case ImportView.GENERATE_ROADMAP:
                controller.generateRoadMap();
                break;
            case RequestManagerView.DELETE_REQUEST:
                controller.deleteRequest(null);
                break;
            case RequestManagerView.ADD_REQUEST:
                controller.addRequest();
                break;
            case RequestManagerView.REDO:
                controller.redo();
                break;
            case RequestManagerView.UNDO:
                controller.undo();
                break;
        }

    }
}