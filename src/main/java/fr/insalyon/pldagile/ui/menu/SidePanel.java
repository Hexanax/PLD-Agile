package fr.insalyon.pldagile.ui.menu;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class SidePanel extends Region {

    public ImportView getImportView() {
        return importView;
    }

    public PickupView getPickupView() {
        return pickupView;
    }

    private final ImportView importView;
    private final PickupView pickupView;

    public SidePanel() {
        importView = new ImportView();
        pickupView = new PickupView();
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(importView);
        mainBorderPane.setCenter(pickupView);
        this.getChildren().add(mainBorderPane);
    }

}
