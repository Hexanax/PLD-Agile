package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class RequestView extends Region {
    protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";
    private final Controller controller;

    private final ListView<AddressItem> requestList;

    public RequestView(Controller controller){
        this.controller = controller;
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
        requestList = new ListView<>();
        requestList.getStyleClass().add("requests-list");
        requestList.setOrientation(Orientation.VERTICAL);
        requestList.setMaxHeight(200D);
        gridPane.add(requestList, 0, 1, 2, 1);

        Button addRequest = new Button(ADD_REQUEST);
        createButton(addRequest, gridPane,0, 2, 1, 1);
        Button deleteRequest = new Button(DELETE_REQUEST);
        createButton(deleteRequest, gridPane,1, 2, 1, 1);
        Button redo = new Button(REDO);
        createButton(redo, gridPane,0, 3, 1, 1);
        Button undo = new Button(UNDO);
        createButton(undo, gridPane,1, 3, 1, 1);


        requestList.setOnMouseClicked(MouseListener::mouseClicked);
        this.getChildren().add(gridPane);
    }

    private void createButton(Button button, GridPane gridPane, int columnIndex, int rowIndex, int colspan, int rowspan){
        //button.setGraphic(IconProvider.getIcon(IMPORT_ICON, 17));
        gridPane.add(button, columnIndex, rowIndex, colspan, rowspan);
        button.setOnAction(ButtonListener::actionPerformed);
    }

    public void setView(ObservableList<AddressItem> list) {
        requestList.setItems(list);
    }

    public ListView<AddressItem> getRequestList() {
        return requestList;
    }

    private EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getClickCount() == 2) {
                controller.modifyClick(requestList.getSelectionModel().getSelectedItem().getRequestNumber(), requestList.getSelectionModel().getSelectedItem().getType(), requestList.getSelectionModel().getSelectedItem().getStepIndex());
            }
        }
    };

    public void activeRowListener() {
        requestList.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public void disableRowListener() {
        requestList.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }
}
