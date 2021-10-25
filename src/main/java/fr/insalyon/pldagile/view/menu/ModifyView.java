package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ModifyView extends Region {
    private static Controller controller = null;

    private Button addRequest;
    private Button deleteRequest;

    public ModifyView(Controller controller) {
        this.controller = controller;

        //TODO Move outside of constructor with function calls
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Import");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(titleLabel, 0,0,2,1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0,10,0));

        addRequest = new Button("Add Request");
        deleteRequest = new Button("Delete Request");
        gridPane.add(addRequest, 0, 1, 1, 1);
        gridPane.add(deleteRequest, 1, 1, 1, 1);
    }
}
