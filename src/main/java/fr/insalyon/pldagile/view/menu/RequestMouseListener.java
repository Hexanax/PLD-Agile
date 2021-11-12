package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Static mouse listener, listening to mouse listeners and dispatching them to the controller
 */
public class RequestMouseListener {

    private static Controller controller;

    public RequestMouseListener(Controller controller) {
    }

    public static void setController(Controller newController) {
        controller = newController;
    }

    public static void mouseClicked(MouseEvent event) {
        if (event.getSource() instanceof ListView) {
            ListView<AddressItem> addressItemListView = (ListView<AddressItem>) event.getSource();
            AddressItem item = addressItemListView.getSelectionModel().getSelectedItem();
            if (item != null && event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                controller.modifyClick(item.getRequestNumber(), item.getType(), item.getStepIndex());
            }
        }
    }

}