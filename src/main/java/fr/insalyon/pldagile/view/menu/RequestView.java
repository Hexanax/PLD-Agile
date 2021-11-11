package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.view.IconProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class RequestView extends Region {
    protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";
    protected static final String ADD_REQUEST_ICON = "plus";
    protected static final String DELETE_REQUEST_ICON = "delete";
    protected static final String UNDO_ICON = "undo";
    protected static final String REDO_ICON = "redo";
    private ObservableList<AddressItem> addressItems = FXCollections.observableArrayList();

    private final ListView<AddressItem> requestList;

    public RequestView(){
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setPrefWidth(Double.POSITIVE_INFINITY);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        // Title Label
        Label titleLabel = new Label("Requests");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        // Buttons
        Button addRequest = new Button(ADD_REQUEST);
        createButton(addRequest, gridPane, 0, 1, 1, 1);
        addRequest.setGraphic(IconProvider.getIcon(ADD_REQUEST_ICON, 17));
        addRequest.getStyleClass().add("add-button");

        Button deleteRequest = new Button(DELETE_REQUEST);
        createButton(deleteRequest, gridPane, 1, 1, 1, 1);
        deleteRequest.setGraphic(IconProvider.getIcon(DELETE_REQUEST_ICON, 17));
        deleteRequest.getStyleClass().add("delete-button");
        Button undo = new Button(UNDO);
        createButton(undo, gridPane, 0, 2, 1, 1);
        undo.setGraphic(IconProvider.getIcon(UNDO_ICON, 17));

        Button redo = new Button(REDO);
        createButton(redo, gridPane, 1, 2, 1, 1);
        redo.setGraphic(IconProvider.getIcon(REDO_ICON, 17));

        // List of steps
        requestList = new ListView<>();
        requestList.getStyleClass().add("requests-list");
        requestList.setPrefWidth(Double.POSITIVE_INFINITY);
        requestList.setOrientation(Orientation.VERTICAL);

        gridPane.add(requestList, 0, 3, 2, 1);


        requestList.setOnMouseClicked(MouseListener::mouseClicked);
        this.getChildren().add(gridPane);
    }

    private void createButton(Button button, GridPane gridPane, int columnIndex, int rowIndex, int colspan,
            int rowspan) {
        gridPane.add(button, columnIndex, rowIndex, colspan, rowspan);
        button.setOnAction(ButtonListener::actionPerformed);
    }

    public void setView(ObservableList<AddressItem> list) {
        if (list.size() == 0) {
            return;
        }
        addressItems.clear();
        addressItems.addAll(list);
        for (AddressItem addressItem : list) {
            addressItem.enforceHeight();
        }
        double listHeight = 0;
        for (AddressItem addressItem : list) {
            listHeight += addressItem.getAddressItemHeight();
        }
        requestList.setPrefHeight(listHeight * 1.1);
        requestList.setItems(addressItems);
    }

    public ListView<AddressItem> getRequestList() {
        return requestList;
    }

    public void setFirstFocus(AddressItem item, int index) {
        requestList.scrollTo(item);
        requestList.getSelectionModel().select(index + 2);
        // requestList.getSelectionModel().getSelectedItem().setStyle("-fx-background-color:
        // #3C8AFF");
    }

    public void setHover(AddressItem item) {
        requestList.getSelectionModel().select(item);
    }
}
