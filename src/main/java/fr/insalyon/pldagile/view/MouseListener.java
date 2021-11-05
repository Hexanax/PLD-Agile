package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;

import javafx.scene.input.MouseEvent;

public class MouseListener {
    private static Controller controller;

    public MouseListener(Controller controller) {
        MouseListener.controller = controller;
    }

    public static void mouseClicked(MouseEvent event) {
        //System.out.println(event.getTarget().getClass().getSimpleName());
        controller.leftClick(event);
    }
}
