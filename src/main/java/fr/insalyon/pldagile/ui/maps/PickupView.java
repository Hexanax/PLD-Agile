package fr.insalyon.pldagile.ui.maps;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class PickupView extends Region {

    private Button addRequest;

    public PickupView() {
        Label title = new Label("Pickup List");
        GridPane maingp = new GridPane();
        GridPane list = new GridPane();
        addRequest = new Button("Add Request");
        maingp.add(title, 0, 0);
        maingp.add(list, 0, 1);
        maingp.add(addRequest, 0, 2);
        this.getChildren().add(maingp);
    }
}
