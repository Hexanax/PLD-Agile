package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.input.KeyEvent;


public class KeyboardListener {

    private static Controller controller;

    public KeyboardListener(Controller controller) {
        KeyboardListener.controller = controller;
    }

    public static void keyPressed(KeyEvent event) {
        controller.keystroke(event.getCode(), event.isControlDown());
    }
}