package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseListener {
    private static Controller controller;

    public static void setController(Controller newController) {
        controller = newController;
    }

    public static void mouseClicked(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY){
            controller.confirm();
        }
        if(event.getButton() == MouseButton.SECONDARY){
            controller.cancel();
        }

    }

    public static void MouseHover(MouseEvent event){

    }
}