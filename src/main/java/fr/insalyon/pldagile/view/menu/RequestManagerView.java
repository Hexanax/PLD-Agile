package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.IconProvider;
import fr.insalyon.pldagile.view.View;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class RequestManagerView extends Region implements View {

    protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";
    protected static final String ADD_REQUEST_ICON = "plus";
    protected static final String DELETE_REQUEST_ICON = "delete";
    protected static final String UNDO_ICON = "undo";
    protected static final String REDO_ICON = "redo";

    private GridPane gridPane;

    public RequestManagerView(Controller controller) {
        render();
    }

    private void createButton(Button button, GridPane gridPane, int columnIndex, int rowIndex, int colspan, int rowspan) {
        gridPane.add(button, columnIndex, rowIndex, colspan, rowspan);
        button.setOnAction(MenuButtonListener::actionPerformed);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    @Override
    public void render() {
        gridPane = new GridPane();
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

        this.getChildren().add(gridPane);
    }

}
