package fr.insalyon.pldagile.view.menu;

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

    public SidePanel() {
        importView = new ImportView();
        pickupView = new RequestView();
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(importView);
        mainBorderPane.setCenter(pickupView);
        this.getChildren().add(mainBorderPane);
    }

}
