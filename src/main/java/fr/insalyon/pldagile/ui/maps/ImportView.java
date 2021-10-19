package fr.insalyon.pldagile.ui.maps;

import fr.insalyon.pldagile.PickyApplication;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ImportView extends Region {

    private Button importMapButton;
    private Button importPickupButton;
    private Button computeButton;
    private Label importMapLabel;
    private Label importPickupLabel;



    public ImportView () {

        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.CENTER);
        maingp.setPadding(new Insets(10, 10, 10, 10));
        maingp.setHgap(10);
        maingp.setVgap(10);

        Label titleLabel = new Label("Import");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        maingp.add(titleLabel, 0,0,2,1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0,10,0));

        importMapButton = new Button("Import map");
        importPickupButton = new Button("Import Requests");
        maingp.add(importMapButton, 0, 1, 1, 1);
        maingp.add(importPickupButton, 1, 1, 1, 1);

        importMapLabel = new Label("No file imported yet");
        importPickupLabel = new Label("No file imported yet");
        maingp.add(importMapLabel, 0, 2, 1, 1);
        maingp.add(importPickupLabel, 1, 2, 1, 1);


        computeButton = new Button("Compute tour");
        computeButton.setPrefHeight(40);
        computeButton.setDefaultButton(true);
        computeButton.setPrefWidth(100);
        maingp.add(computeButton, 0, 3, 2, 1);
        GridPane.setHalignment(computeButton, HPos.CENTER);
        GridPane.setMargin(computeButton, new Insets(10, 0,0,0));

        this.getChildren().add(maingp);

        importMapButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    XMLDeserializer.load(PickyApplication.getCityMap());
                    PickyApplication.updateCityMap();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        importPickupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    XMLDeserializer.load(PickyApplication.getPlanningRequest(), PickyApplication.getCityMap());
                    PickyApplication.updatePlanningRequest();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        computeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });


    }
    public Button getImportMapButton() {
        return importMapButton;
    }

    public Button getImportPickupButton() {
        return importPickupButton;
    }

    public Button getComputeButton() {
        return computeButton;
    }


}
