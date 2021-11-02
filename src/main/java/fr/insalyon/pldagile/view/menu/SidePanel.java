package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.ListView;

public class SidePanel extends Region {
    private Controller controller;

    private VBox mainBorderPane;
    public double SIDE_PANEL_WIDTH = 350;

    public SidePanel(Controller controller) {
        this.controller = controller;
        this.mainBorderPane = new VBox();
        mainBorderPane.setMinWidth(SIDE_PANEL_WIDTH);
    }

    public void MainSidePanel(double height) {
        ListView<Region> listView = new ListView<>();

        ObservableList<Region> regions = FXCollections.observableArrayList();
        regions.add(new ImportView(controller));
        regions.add(new RequestView(controller));

        listView.setOrientation(Orientation.VERTICAL);
        listView.setItems(regions);
        listView.setPrefHeight(height);
        listView.setFocusTraversable(false);

        mainBorderPane.getStyleClass().add("side-panel");
        mainBorderPane.getChildren().add(listView);
        mainBorderPane.setAlignment(Pos.CENTER);
        VBox.setVgrow(mainBorderPane, Priority.ALWAYS);
        this.getChildren().add(mainBorderPane);
    }

    public void ModifyPanel() {
        ObservableList<Region> regions = FXCollections.observableArrayList();
        regions.add(new ModifyView(controller));
        ListView<Region> listView = new ListView<>();

        listView.setItems(regions);
        mainBorderPane.getChildren().add(listView);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }

    public VBox getMainBorderPane() {
        return mainBorderPane;
    }
}
