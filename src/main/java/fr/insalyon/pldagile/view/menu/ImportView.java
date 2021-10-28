package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ImportView extends Region {

    private static Controller controller = null;

    private Button importMapButton;
    private Button importPickupButton;
    private Button computeButton;
    private Label importMapLabel;
    private Label importPickupLabel;

    protected static final String IMPORT_TITLE = "Imports";
    protected static final String LOAD_MAP = "Import map";
    protected static final String LOAD_REQUESTS = "Import Requests";
    protected static final String COMPUTE_TOUR = "Compute tour";

    protected static final String IMPORT_ICON = "import";
    protected static final String COMPUTE_ICON = "compute";



    private final String[] buttonTexts = new String[]{LOAD_MAP, LOAD_REQUESTS, COMPUTE_TOUR};

    public ImportView (Controller controller) {
        ImportView.controller = controller;

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        Label titleLabel = new Label(IMPORT_TITLE);
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0,0,2,1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        importMapButton = new Button(LOAD_MAP);
        importMapButton.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        importPickupButton = new Button(LOAD_REQUESTS);
        importPickupButton.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        gridPane.add(importMapButton, 0, 1, 1, 1);
        gridPane.add(importPickupButton, 1, 1, 1, 1);

        // TODO: put the name of file once it is imported
        importMapLabel = new Label("No file imported yet");
        importPickupLabel = new Label("No file imported yet");
        gridPane.add(importMapLabel, 0, 2, 1, 1);
        gridPane.add(importPickupLabel, 1, 2, 1, 1);

        // COMPUTE BUTTON
        computeButton = new Button(COMPUTE_TOUR);
        computeButton.setDefaultButton(true);
        computeButton.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        gridPane.add(computeButton, 0, 3, 2, 1);
        computeButton.getStyleClass().add("main-button");
        GridPane.setHalignment(computeButton, HPos.CENTER);
        GridPane.setMargin(computeButton, new Insets(10, 0,0,0));
        computeButton.setOnAction(this::computeTour);

        this.getChildren().add(gridPane);


        importMapButton.setOnAction(this::computeTour);
        importPickupButton.setOnAction(this::computeTour);

    }
    public Button getImportMapButton() {
        return importMapButton;
    }

    public Button getImportPickupButton() {
        return importPickupButton;
    }

    public Button getComputeIcon() {
        return computeButton;
    }

    private void computeTour(ActionEvent event){
        switch (((Button) event.getTarget()).getText()){
            case LOAD_MAP:
                controller.loadMap(); break;
            case LOAD_REQUESTS:
                controller.loadRequests(); break;
            case COMPUTE_TOUR:
                controller.computeTour(); break;
        }

    }

}