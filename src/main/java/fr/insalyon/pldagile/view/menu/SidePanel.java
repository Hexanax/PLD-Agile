package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class SidePanel extends Region {

    public ImportView getImportView() {
        return importView;
    }

    public RequestView getPickupView() {
        return pickupView;
    }

    private final ImportView importView;
    private final RequestView pickupView;

    public SidePanel(Controller controller) {
        importView = new ImportView(controller);
        pickupView = new RequestView(controller);
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(importView);
        mainBorderPane.setCenter(pickupView);
        this.getChildren().add(mainBorderPane);
    }

}
