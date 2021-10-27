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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.awt.*;
import java.util.List;

public class RequestView extends Region {

    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();
    private Button addRequestButton;
    private Button generateRoadMap;

    public RequestView(Controller controller) {
        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_LEFT);

        Label titleLabel = new Label("Requests");
        titleLabel.getStyleClass().add("h1");
        maingp.add(titleLabel, 0, 0, 1, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        ListView<RequestItem> pickupList = new ListView<>();
        pickupList.setItems(pickupItems);
        pickupList.getStyleClass().add("requests-list");
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        maingp.add(pickupList, 0, 2, 1, 1);


        addRequestButton = new Button("Modify the tour");
        addRequestButton.setDefaultButton(true);
        generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setDefaultButton(true);
        generateRoadMap.getStyleClass().add("main-button");

        maingp.add(addRequestButton, 0, 3, 1, 1);
        GridPane.setHalignment(addRequestButton, HPos.LEFT);
        GridPane.setMargin(addRequestButton, new Insets(24, 0, 0, 0));

        maingp.add(generateRoadMap, 0, 5, 1, 1);
        GridPane.setHalignment(generateRoadMap, HPos.LEFT);
        GridPane.setMargin(generateRoadMap, new Insets(24, 0, 20, 0));

        this.getChildren().add(maingp);

        addRequestButton.setOnAction(event -> {
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
