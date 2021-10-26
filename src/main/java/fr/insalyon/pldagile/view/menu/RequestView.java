package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class RequestView extends Region {

    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();
    private Button addRequestButton;
    private Button generateRoadMap;

    public RequestView(Controller controller) {
        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.CENTER);
        maingp.setPadding(new Insets(40, 40, 40, 40));
        maingp.setHgap(10);
        maingp.setVgap(10);

        Label titleLabel = new Label("Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        maingp.add(titleLabel, 0, 0, 1, 1);
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setMargin(titleLabel, new Insets(20, 0, 20, 0));

        ListView<RequestItem> pickupList = new ListView<RequestItem>();
        pickupList.setItems(pickupItems);
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        maingp.add(pickupList, 0, 2, 1, 1);


        addRequestButton = new Button("Modify the tour");
        addRequestButton.setPrefHeight(40);
        addRequestButton.setDefaultButton(true);
        addRequestButton.setPrefWidth(100);
        generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setPrefHeight(40);
        generateRoadMap.setDefaultButton(true);
        generateRoadMap.setPrefWidth(200);

        maingp.add(addRequestButton, 0, 3, 1, 1);
        GridPane.setHalignment(addRequestButton, HPos.CENTER);
        GridPane.setMargin(addRequestButton, new Insets(20, 0, 20, 0));

        maingp.add(generateRoadMap, 0, 5, 1, 1);
        GridPane.setHalignment(generateRoadMap, HPos.CENTER);
        GridPane.setMargin(generateRoadMap, new Insets(20, 0, 20, 0));

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
