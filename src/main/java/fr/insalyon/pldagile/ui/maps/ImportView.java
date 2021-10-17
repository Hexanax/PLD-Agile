package fr.insalyon.pldagile.ui.maps;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ImportView extends Region {
    private Button importMap;
    private Button importPickup;
    private Button compute;

    public ImportView () {
        GridPane maingp = new GridPane();
        Label titleLabel = new Label("Import");
        GridPane buttonPane = new GridPane();
        importMap = new Button("Import map");
        importPickup = new Button("Import Requests");
        buttonPane.add(importMap, 0, 0);
        buttonPane.add(importPickup, 1, 0);
        compute = new Button("Compute tour");
        maingp.add(titleLabel, 0, 0);
        maingp.add(buttonPane, 0, 1);
        maingp.add(compute, 0, 2);
        maingp.setHgap(10);
        maingp.setVgap(10);
        this.getChildren().add(maingp);

    }
}
