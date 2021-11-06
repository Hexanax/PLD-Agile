package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.IconProvider;
import fr.insalyon.pldagile.view.maps.MapPoint;
import javafx.collections.FXCollections;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//public class RequestMenuView extends Region implements PropertyChangeListener {

    /*protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";

    private Controller controller;
    private final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList(); //TODO Move it elsewhere
    private final ListView<RequestItem> pickupList;

    private final String[] buttonTexts = new String[]{DELETE_REQUEST, ADD_REQUEST, UNDO, REDO};

    public RequestMenuView(Controller controller) {
        GridPane gridPane = new GridPane();
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
        pickupList = new ListView<>();
        pickupList.setItems(pickupItems);
        pickupList.getStyleClass().add("requests-list");
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(200D);
        gridPane.add(pickupList, 0, 1, 2, 1);

        // Generate RoadMap Button
        Button generateRoadMap = new Button("Generate the Road Map");
        generateRoadMap.setGraphic(IconProvider.getIcon(COMPUTE_ICON, 20));
        generateRoadMap.getStyleClass().add("main-button");
        generateRoadMap.setDefaultButton(true);
        gridPane.add(generateRoadMap, 0, 4, 2, 1);
        GridPane.setHalignment(generateRoadMap, HPos.CENTER);

        generateRoadMap.setOnAction(event -> {
            controller.generateRoadMap();
        });

        Button addRequest = new Button(ADD_REQUEST);
        Button deleteRequest = new Button(DELETE_REQUEST);
        Button redo = new Button(REDO);
        Button undo = new Button(UNDO);

        gridPane.add(addRequest, 0, 2, 1, 1);
        gridPane.add(deleteRequest, 1, 2, 1, 1);
        gridPane.add(undo, 0, 3, 1, 1);
        gridPane.add(redo, 1, 3, 1, 1);

        deleteRequest.setOnAction(this::actionPerformed);
        addRequest.setOnAction(this::actionPerformed);
        undo.setOnAction(this::actionPerformed);
        redo.setOnAction(this::actionPerformed);
        this.getChildren().add(gridPane);
    }

    private void actionPerformed(ActionEvent event) {
        switch (((Button) event.getTarget()).getText()) {
            case DELETE_REQUEST:
                controller.deleteRequest(null);
                break;
            case ADD_REQUEST:
                controller.addRequest(null);
                break;
            case REDO:
                controller.redo();
                break;
            case UNDO:
                controller.undo();
                break;
        }
    }*/

   /* public void renderRequestMenu(PlanningRequest planningRequest) {
        if (!planningRequest.getRequests().isEmpty() && planningRequest.getDepot() != null) {
            // Render the planning request
            Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
            MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
            depotPoint.setId(planningRequest.getDepot().getIntersection().getId());
            ArrayList<RequestItem> items = new ArrayList<>();
            planningRequest.getRequests().forEach(request -> {
                // Items in list
                Pickup pickup = request.getPickup();
                RequestItem pickupItem = new RequestItem("Pickup at " + request.getPickup().getIntersection().getId(), "Duration: " + request.getPickup().getDuration(), request.getId(), "Pickup", -1);
                Delivery delivery = request.getDelivery();
                RequestItem deliveryItem = new RequestItem("Delivery at " + request.getDelivery().getIntersection().getId(), "Duration: " + request.getDelivery().getDuration(), request.getId(), "Delivery", -1);
                items.add(pickupItem);
                items.add(deliveryItem);
            });
            setPickupItems(items);
        }
    }

    public void orderListRequests(ArrayList<Pair<Long, String>> steps, Map<Long, Request> requests, Depot depot) {
        ArrayList<RequestItem> items = new ArrayList<>();
        int index = 0;
        RequestItem item = new RequestItem("Depot at " + depot.getIntersection().getId(), "Departure time : " + depot.getDepartureTime(), -1, "Depot", 0);
        items.add(item);
        for (Pair<Long, String> step : steps) {
            if (Objects.equals(step.getValue(), "pickup")) {
                item = new RequestItem("Pickup at " + requests.get(step.getKey()).getPickup().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getPickup().getDuration(), step.getKey(), "Pickup", index);
                items.add(item);
            }
            if (Objects.equals(step.getValue(), "delivery")) {
                item = new RequestItem("Delivery at " + requests.get(step.getKey()).getDelivery().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getDelivery().getDuration(), step.getKey(), "Delivery", index);
                items.add(item);
            }

            index++;
        }
        item = new RequestItem("Depot at " + depot.getIntersection().getId(), "", -2, "Depot", (index - 1));
        items.add(item);
        clearItems();

        setPickupItems(items);

    }

    public void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public void clearItems() {
        pickupItems.clear();
    }*/

//    private static EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent e) {
//            if (e.getClickCount() == 2) {
//                controller.modifyClick(pickupList.getSelectionModel().getSelectedItem().getRequestNumber(), pickupList.getSelectionModel().getSelectedItem().getType(), pickupList.getSelectionModel().getSelectedItem().getStepIndex());
//            }
//        }
//    };
//
//    public void activeRowListener() {
//        pickupList.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
//    }
//
//    public void disableRowListener() {
//        pickupList.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
//    }
//
   /* @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("tourUpdate")){
            Tour newTourValue = (Tour) evt.getNewValue();
            clearItems();
            orderListRequests(newTourValue.getSteps(), newTourValue.getRequests(), newTourValue.getDepot());
        }
    }*/
//}
