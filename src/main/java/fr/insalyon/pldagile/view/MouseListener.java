package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * This class implements the mouseClicked event
 */
public class MouseListener {

    private static Controller controller;

    public static void setController(Controller newController) {
        controller = newController;
    }


    /**
     * checks the mouse clicked button (left for PRIMARY and right for SECONDARY)
     * listener and directs the controller depending on the button
     * @param event
     */
    public static void mouseClicked(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY){
            controller.confirm();
        }
        if(event.getButton() == MouseButton.SECONDARY){
            controller.cancel();
        }

    }

}