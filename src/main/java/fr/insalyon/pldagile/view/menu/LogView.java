package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.List;

public class LogView extends Region {


    private static final ObservableList<TextItem> textItems = FXCollections.observableArrayList();
    private static ListView<TextItem> textList;

    public LogView (){


        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("side-panel-section");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(16);
        gridPane.setVgap(16);

        // Title Label
        Label titleLabel = new Label("State");
        titleLabel.getStyleClass().add("h1");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        // List of steps
        textList = new ListView<>();
        textList.setItems(textItems);
        textList.getStyleClass().add("requests-list");
        textList.setOrientation(Orientation.VERTICAL);
        textList.setMaxHeight(80);
        gridPane.add(textList, 0, 1, 2, 1);


        this.getChildren().add(gridPane);
    }

    public static void setTextItems(List<TextItem> textList) {
        clearItems();
        textItems.addAll(textList);
    }

    public static void addTextItem(TextItem textItem) {
        textItems.add(textItem);
        textList.scrollTo(textItem);
    }

    public static void clearItems() {
        textItems.clear();
    }

}
