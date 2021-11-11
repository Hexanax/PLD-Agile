package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.input.KeyEvent;


public class KeyboardListener {

    private static Controller controller;

    public static void setController(Controller newController) {
        controller = newController;
    }

    public static void keyPressed(KeyEvent event) {
        controller.keystroke(event.getCode(), event.isControlDown());
    }
}