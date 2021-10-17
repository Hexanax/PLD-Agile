package fr.insalyon.pldagile.ui.maps;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class SidePanel extends Region {

    private ImportView iview;
    private PickupView pview;

    public SidePanel() {
        iview = new ImportView();
        pview = new PickupView();
        GridPane maingp = new GridPane();
        maingp.add(iview, 0, 0);
        maingp.add(pview, 0, 1);
        this.getChildren().add(maingp);
    }

}
