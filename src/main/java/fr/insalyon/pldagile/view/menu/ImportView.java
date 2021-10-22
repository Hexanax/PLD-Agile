package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.PickyApplication;
import fr.insalyon.pldagile.xml.FileChooseOption;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class ImportView extends Region {

    private Button importMapButton;
    private Button importPickupButton;
    private Button computeButton;
    private Label importMapLabel;
    private Label importPickupLabel;

    public ImportView () {
        //TODO Move outside of constructor with function calls
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Import");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
<<<<<<< HEAD:src/main/java/fr/insalyon/pldagile/view/menu/ImportView.java
        gridPane.add(titleLabel, 0,0,2,1);
=======
        maingp.add(titleLabel, 0,0,3,1);
>>>>>>> ihm:src/main/java/fr/insalyon/pldagile/ui/maps/ImportView.java
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(0, 0,10,0));

        importMapButton = new Button("Import map");
        importPickupButton = new Button("Import Requests");
<<<<<<< HEAD:src/main/java/fr/insalyon/pldagile/view/menu/ImportView.java
        gridPane.add(importMapButton, 0, 1, 1, 1);
        gridPane.add(importPickupButton, 1, 1, 1, 1);

        importMapLabel = new Label("No file imported yet");
        importPickupLabel = new Label("No file imported yet");
        gridPane.add(importMapLabel, 0, 2, 1, 1);
        gridPane.add(importPickupLabel, 1, 2, 1, 1);
=======
        maingp.add(importMapButton, 0, 1, 1, 1);
        maingp.add(importPickupButton, 2, 1, 1, 1);

        importMapLabel = new Label("No file imported yet");
        importPickupLabel = new Label("No file imported yet");
        maingp.add(importMapLabel, 0, 2, 1, 1);
        maingp.add(importPickupLabel, 2, 2, 1, 1);
>>>>>>> ihm:src/main/java/fr/insalyon/pldagile/ui/maps/ImportView.java


        computeButton = new Button("Compute tour");
        computeButton.setPrefHeight(40);
        computeButton.setDefaultButton(true);
        computeButton.setPrefWidth(100);
<<<<<<< HEAD:src/main/java/fr/insalyon/pldagile/view/menu/ImportView.java
        gridPane.add(computeButton, 0, 3, 2, 1);
        GridPane.setHalignment(computeButton, HPos.CENTER);
        GridPane.setMargin(computeButton, new Insets(10, 0,0,0));

        //TODO Start TSP resolution algorithm
        //TODO Move in controller for handling and action setup
        computeButton.setOnAction(event -> {
            System.out.println("Computing tour...");
            PickyApplication.renderTour();
=======
        maingp.add(computeButton, 0, 3, 3, 1);
        GridPane.setHalignment(computeButton, HPos.CENTER);
        GridPane.setMargin(computeButton, new Insets(10, 0,0,0));

        this.getChildren().add(maingp);

        importMapButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    XMLDeserializer.load(PickyApplication.getCityMap());
                    PickyApplication.updateCityMap();
                    importMapLabel.setText("");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
>>>>>>> ihm:src/main/java/fr/insalyon/pldagile/ui/maps/ImportView.java
        });

        this.getChildren().add(gridPane);

        //TODO Move in controller for handling and action setup
        importMapButton.setOnAction(event -> {
            try {
                File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
                PickyApplication.emptyCityMap();
                PickyApplication.emptyPlanningRequest(); //New loaded map means we reset the requests too
                XMLDeserializer.load(PickyApplication.getCityMap(), importFile); //TODO Handle empty city & requests as load file success callback to prevent the "cancel" and invalid format from resetting
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                PickyApplication.clearMap();
                PickyApplication.renderMapAndRequests();
            }
        });

        //TODO Move in controller for handling and action setup
        importPickupButton.setOnAction(event -> {
            try {
                File importFile = XMLFileOpener.getInstance().open(FileChooseOption.READ);
                PickyApplication.emptyPlanningRequest();
                XMLDeserializer.load(PickyApplication.getPlanningRequest(), PickyApplication.getCityMap(), importFile); //TODO Handle emptyPlanningRequest as load file success callback to prevent the "cancel" and invalid format from resetting
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                PickyApplication.clearMap();
                PickyApplication.renderMapAndRequests();
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
