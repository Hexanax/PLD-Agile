package fr.insalyon.pldagile.ui.maps;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        maingp.setAlignment(Pos.CENTER_RIGHT);
        maingp.setPadding(new Insets(0, 0, 0, 0));
        maingp.add(iview, 0, 0, 1, 1);
        maingp.add(pview, 0, 1, 1, 3);
        this.getChildren().add(maingp);
    }

}
