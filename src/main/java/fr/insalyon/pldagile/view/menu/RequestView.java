package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.List;

public class RequestView extends Region {

    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();

    protected static final String MODIFY_ICON = "edit";
    protected static final String COMPUTE_ICON = "compute";

    public RequestView(Controller controller) {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        // Title Label
        Label titleLabel = new Label("Requests");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        // List of steps
        ListView<RequestItem> pickupList = new ListView<>();
        pickupList.setItems(pickupItems);
        pickupList.getStyleClass().add("requests-list");
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        gridPane.add(pickupList, 0, 1, 2, 1);


        // Modify Tour Button
        Button modifyTourButton = new Button("Modify the tour");
        modifyTourButton.setGraphic(IconProvider.getIcon(MODIFY_ICON, 18));
        modifyTourButton.setDefaultButton(true);
        gridPane.add(modifyTourButton, 0, 2, 1, 1);
        GridPane.setHalignment(modifyTourButton, HPos.LEFT);

        modifyTourButton.setOnAction(event -> {
            controller.modify();
        });

        // Generate RoadMap Button
        Button generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        generateRoadMap.getStyleClass().add("main-button");
        generateRoadMap.setDefaultButton(true);
        gridPane.add(generateRoadMap, 0, 3, 2, 1);
        GridPane.setHalignment(generateRoadMap, HPos.CENTER);

        generateRoadMap.setOnAction(event -> {
            controller.generateRoadMap();
        });


        this.getChildren().add(gridPane);


    }

    public static void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public static void clearItems() {
        pickupItems.clear();
    }
}
