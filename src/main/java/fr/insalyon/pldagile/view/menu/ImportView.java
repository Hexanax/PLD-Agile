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

    private final Button importMapButton;
    private final Button importPickupButton;
    private final Button computeButton;

    protected static final String IMPORT_TITLE = "Imports";

    protected static final String LOAD_MAP = "Import map";
    protected static final String LOAD_REQUESTS = "Import Requests";
    protected static final String COMPUTE_TOUR = "Compute tour";

    protected static final String NO_FILE_IMPORTED_MESSAGE = "No file imported yet";

    protected static final String IMPORT_ICON = "import";
    protected static final String COMPUTE_ICON = "compute";


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

        // Import Map Button
        importMapButton = new Button(LOAD_MAP);
        importMapButton.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        gridPane.add(importMapButton, 0, 1, 1, 1);

        importMapButton.setOnAction(this::computeTour);

        Label importMapLabel = new Label(NO_FILE_IMPORTED_MESSAGE);
        gridPane.add(importMapLabel, 0, 2, 1, 1);


        // Import Pickup Button
        importPickupButton = new Button(LOAD_REQUESTS);
        importPickupButton.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        gridPane.add(importPickupButton, 1, 1, 1, 1);

        importPickupButton.setOnAction(this::computeTour);

        Label importPickupLabel = new Label(NO_FILE_IMPORTED_MESSAGE);
        gridPane.add(importPickupLabel, 1, 2, 1, 1);

        // COMPUTE BUTTON
        computeButton = new Button(COMPUTE_TOUR);
        computeButton.setDefaultButton(true);
        computeButton.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        gridPane.add(computeButton, 0, 3, 2, 1);
        computeButton.getStyleClass().add("main-button");
        computeButton.setOnAction(this::computeTour);
        GridPane.setHalignment(computeButton, HPos.CENTER);
        GridPane.setMargin(computeButton, new Insets(10, 0,0,0));

        this.getChildren().add(gridPane);
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