package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.IconProvider;
import fr.insalyon.pldagile.view.maps.PointLayer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.awt.*;
import java.util.List;

public class RequestView extends Region {

    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();
    private static ListView<RequestItem> pickupList;

    protected static final String MODIFY_ICON = "edit";
    protected static final String COMPUTE_ICON = "compute";

    private static Controller controller;

    private Button addRequest;
    private Button deleteRequest;
    private Button redo;
    private Button undo;
    protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";

    private final String[] buttonTexts = new String[]{DELETE_REQUEST, ADD_REQUEST, UNDO, REDO};



    public RequestView(Controller controller) {

        this.controller = controller;
        render();
        pickupItems.addListener(new ListChangeListener<RequestItem>() {

            @Override
            public void onChanged(Change<? extends RequestItem> c) {
                render();
            }
        });

    }

    private void render() {
        if (pickupItems.isEmpty()) {
            return;
        }
        GridPane gridPane = new GridPane();
        gridPane.getChildren().removeAll();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        // Title Label
        Label titleLabel = new Label("Requests");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        // List of steps
        int colIndex = 0;
        for (RequestItem r : pickupItems) {
            gridPane.add(r, 0, ++colIndex, 2, 1);
        }

        addRequest = new Button(ADD_REQUEST);
        deleteRequest = new Button(DELETE_REQUEST);
        redo = new Button(REDO);
        undo = new Button(UNDO);


        gridPane.add(addRequest, 0, ++colIndex, 1, 1);
        gridPane.add(deleteRequest, 1, colIndex, 1, 1);
        gridPane.add(undo, 0, ++colIndex, 1, 1);
        gridPane.add(redo, 1, colIndex, 1, 1);


            deleteRequest.setOnAction(this::actionPerformed);
            addRequest.setOnAction(this::actionPerformed);
            undo.setOnAction(this::actionPerformed);
            redo.setOnAction(this::actionPerformed);

        // Generate RoadMap Button
        Button generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        generateRoadMap.getStyleClass().add("main-button");
        generateRoadMap.setDefaultButton(true);
        gridPane.add(generateRoadMap, 0, ++colIndex, 2, 1);
        GridPane.setHalignment(generateRoadMap, HPos.CENTER);

        generateRoadMap.setOnAction(event -> {
            controller.generateRoadMap();
        });

        this.getChildren().add(gridPane);
    }
    private void actionPerformed(ActionEvent event) {
        switch (((Button) event.getTarget()).getText()){
            case DELETE_REQUEST:
                controller.deleteRequest(null); break;
            case ADD_REQUEST:
                controller.addRequest(null); break;
            case REDO:
                controller.redo(); break;
            case UNDO:
                controller.undo(); break;
        }
    }





    public static void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public static void clearItems() {
        pickupItems.clear();
    }

    private static EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if(e.getClickCount()==2){
                controller.modifyClick(pickupList.getSelectionModel().getSelectedItem().getRequestNumber(), pickupList.getSelectionModel().getSelectedItem().getType(), pickupList.getSelectionModel().getSelectedItem().getStepIndex());
            }
        }
    };

    private static EventHandler<MouseEvent> onOneClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount()==1){
                PointLayer.highlightIcon(pickupList.getSelectionModel().getSelectedItem().getRequestNumber());
            }

        }
    };

    public static void activeRowListener(){
        pickupList.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public static void disableRowListener(){
        pickupList.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public static void activeItemListener(){
        pickupList.addEventHandler(MouseEvent.MOUSE_CLICKED, onOneClick);
    }


    public static void disableItemListener(){
        pickupList.removeEventHandler(MouseEvent.MOUSE_CLICKED, onOneClick);
        if (PointLayer.getLastHighlighted() != null) {
            PointLayer.unHighlightIcon(PointLayer.getLastHighlighted().getKey().getRequestId());
        }
    }
}
