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
    private Button modifyTourButton;
    private Button generateRoadMap;

    protected static final String MODIFY_ICON = "edit";
    protected static final String COMPUTE_ICON = "compute";


    public RequestView(Controller controller) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BASELINE_LEFT);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        Label titleLabel = new Label("Requests");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 1, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        ListView<RequestItem> pickupList = new ListView<>();
        pickupList.setItems(pickupItems);
        pickupList.getStyleClass().add("requests-list");
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        gridPane.add(pickupList, 0, 2, 1, 1);

        // Modify Tour Button
        modifyTourButton = new Button("Modify the tour");
        modifyTourButton.setDefaultButton(true);
        modifyTourButton.setGraphic(IconProvider.getIcon(MODIFY_ICON, 18));
        gridPane.add(modifyTourButton, 0, 3, 1, 1);
        GridPane.setHalignment(modifyTourButton, HPos.LEFT);
        GridPane.setMargin(modifyTourButton, new Insets(24, 0, 0, 0));

        // Generate RoadMap Button
        generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setDefaultButton(true);
        generateRoadMap.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        generateRoadMap.getStyleClass().add("main-button");
        gridPane.add(generateRoadMap, 0, 5, 1, 1);
        GridPane.setHalignment(generateRoadMap, HPos.LEFT);
        GridPane.setMargin(generateRoadMap, new Insets(24, 0, 20, 0));


        this.getChildren().add(gridPane);

        modifyTourButton.setOnAction(event -> {
            controller.modify();
        });

        generateRoadMap.setOnAction(event -> {
            controller.generateRoadMap();
        });
    }

    public static void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public static void clearItems() {
        pickupItems.clear();
    }
}
