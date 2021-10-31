package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
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

import java.util.List;

public class ModifyView extends Region {
    private static Controller controller = null;
    private static final ObservableList<RequestItem> pickupItems = FXCollections.observableArrayList();
    private static ListView<RequestItem> pickupList;

    protected static final String TITLE = "Modify";

    protected static final String CONFIRM = "Confirm";
    protected static final String BACK = "Cancel";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String DELETE_REQUEST = "Delete Request";


    public ModifyView(Controller controller) {
        ModifyView.controller = controller;

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        Label titleLabel = new Label(TITLE);
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        Button addRequest = new Button(ADD_REQUEST);
        gridPane.add(addRequest, 0, 1, 1, 1);

        Button deleteRequest = new Button(DELETE_REQUEST);
        gridPane.add(deleteRequest, 1, 1, 1, 1);


        Label titleLabelRequests = new Label("Requests");
        titleLabelRequests.getStyleClass().add("request-list-element");
        gridPane.add(titleLabelRequests, 0, 2, 2, 1);
        GridPane.setHalignment(titleLabelRequests, HPos.LEFT);
        pickupList = new ListView<>();
        pickupList.setItems(pickupItems);
        pickupList.setOrientation(Orientation.VERTICAL);
        pickupList.setMaxHeight(Control.USE_PREF_SIZE);
        gridPane.add(pickupList, 0, 3, 2, 1);

        Button backMainMenu = new Button(BACK);
        Button confirmMainMenu = new Button(CONFIRM);
        gridPane.add(backMainMenu, 0, 6, 1, 1);
        gridPane.add(confirmMainMenu, 1, 6, 1, 1);

        // add actions
        backMainMenu.setOnAction(this::actionPerformed);
        confirmMainMenu.setOnAction(this::actionPerformed);
        deleteRequest.setOnAction(this::actionPerformed);
        addRequest.setOnAction(this::actionPerformed);

        this.getChildren().add(gridPane);
    }

    private static final EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if(e.getClickCount()==2){
                controller.modifyClick(pickupList.getSelectionModel().getSelectedItem().getRequestNumber(), pickupList.getSelectionModel().getSelectedItem().getType(), pickupList.getSelectionModel().getSelectedItem().getStepIndex());
            }
        }
    };

    private void actionPerformed(ActionEvent event) {
        switch (((Button) event.getTarget()).getText()){
            case BACK:
                controller.cancel(); break;
            case CONFIRM:
                controller.confirm(""); break;
            case DELETE_REQUEST:
                controller.deleteRequest(null); break;
            case ADD_REQUEST:
                controller.addRequest(null); break;
        }
    }

    public static void setPickupItems(List<RequestItem> requestList) {
        clearItems();
        pickupItems.addAll(requestList);
    }

    public static void clearItems() {
        pickupItems.clear();
    }

    public static void activeRowListener() {
        pickupList.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public static void disableRowListener() {
        pickupList.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

}
