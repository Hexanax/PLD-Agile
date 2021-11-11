package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.List;

public class LogView extends Region {

    private static final ObservableList<TextItem> textItems = FXCollections.observableArrayList();
    private static ListView listView;

    public LogView () {

        //TODO larger
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(8);
        gridPane.setVgap(16);

        // Title Label
        Label titleLabel = new Label("Message");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        //logs display

        listView = new ListView(textItems);
        listView.getStyleClass().add("requests-list");
        listView.setMaxHeight(130);
        listView.setMaxWidth(800);
        gridPane.add(listView, 0, 1, 2, 1);

        this.getChildren().add(gridPane);
    }
    

    public static void addText(TextItem textItem) {
        textItems.add(textItem);
        listView.scrollTo(textItem);
    }

    public static void clearItems() {
        textItems.clear();
    }
}