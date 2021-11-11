package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.PointLayer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseListener {


    private static Controller controller;
    public MouseListener(Controller controller) {
        MouseListener.controller = controller;
    }

    public static void mouseClicked(MouseEvent event) {
        AddressItem item = ((ListView<AddressItem>) event.getSource()).getSelectionModel().getSelectedItem();
        if(item != null && event.getClickCount()==1 && event.getButton()==MouseButton.PRIMARY){
            controller.modifyClick(item.getRequestNumber(), item.getType(), item.getStepIndex());
        }

        if(item != null && event.getClickCount() == 1){
            //TODO : highlightIcon
            //PointLayer.highlightIcon(item.getRequestNumber());
        }
    }
}
