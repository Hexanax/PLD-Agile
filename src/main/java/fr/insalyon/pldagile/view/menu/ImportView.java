package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.IconProvider;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ImportView extends Region {


    private final Button importMapButton;
    private final Button importPickupButton;
    private final Button computeButton;
    private final Button slowComputeButton;
    private final Button generateRoadMap;

    protected static final String IMPORT_TITLE = "Imports";

    protected static final String LOAD_MAP = "Import map";
    protected static final String LOAD_REQUESTS = "Import Requests";
    protected static final String COMPUTE_TOUR = "Compute tour";
    protected static final String SLOW_COMPUTE_TOUR = "Compute tour slowly";

    protected static final String GENERATE_ROADMAP = "Generate the Road Map";

    protected static final String NO_FILE_IMPORTED_MESSAGE = "No file imported yet";

    protected static final String IMPORT_ICON = "import";
    protected static final String COMPUTE_ICON = "compute";

    private static final Label importMapLabel = new Label(NO_FILE_IMPORTED_MESSAGE);
    private static final Label importPickupLabel = new Label(NO_FILE_IMPORTED_MESSAGE);



    public ImportView() {


        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        Label titleLabel = new Label(IMPORT_TITLE);
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);


        // Import Map Button
        importMapButton = new Button(LOAD_MAP);
        createButton(importMapButton, gridPane, 0,1,1,1, false);
        gridPane.add(importMapLabel, 0, 2, 1, 1);


        // Import Pickup Button
        importPickupButton = new Button(LOAD_REQUESTS);
        createButton(importPickupButton, gridPane, 1,1,1,1, false);
        gridPane.add(importPickupLabel, 1, 2, 1, 1);

        // COMPUTE BUTTON
        computeButton = new Button(COMPUTE_TOUR);
        createButton(computeButton, gridPane, 0,3,2,1, true);

        // SLOW COMPUTE BUTTON
        slowComputeButton = new Button(SLOW_COMPUTE_TOUR);
        createButton(slowComputeButton, gridPane, 0,4,2,1, true);

        //Generate roadmap button
        generateRoadMap = new Button(GENERATE_ROADMAP);
        createButton(generateRoadMap, gridPane, 0,5,2,1, true);


        this.getChildren().add(gridPane);
    }



    private void createButton(Button button, GridPane gridPane, int columnIndex, int rowIndex, int colspan, int rowspan, boolean defaultButton){
        if(defaultButton){
            button.setDefaultButton(true);
            button.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
            button.getStyleClass().add("main-button");
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setMargin(button, new Insets(10, 0, 0, 0));
        } else {
            button.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        }

        gridPane.add(button, columnIndex, rowIndex, colspan, rowspan);
        button.setOnAction(ButtonListener::actionPerformed);
    }

    public static void setImportMapLabel(String fileName) {
        importMapLabel.setText(fileName);
    }

    public static void setImportRequestLabel(String fileName) {
        importPickupLabel.setText(fileName);
    }

}