package fr.insalyon.pldagile.ui.maps;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class SidePanel extends Region {

    public ImportView getIview() {
        return iview;
    }

    public PickupView getPview() {
        return pview;
    }

    private ImportView iview;
    private PickupView pview;

    public SidePanel() {
        iview = new ImportView();
        pview = new PickupView();
        BorderPane maingp = new BorderPane();
        maingp.setTop(iview);
        maingp.setCenter(pview);
        this.getChildren().add(maingp);
    }

}
