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
    private static TextArea textarea;

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
        textarea = new TextArea();
        textarea.setEditable(false);
        textarea.getStyleClass().add("requests-list");
        textarea.setMaxHeight(130);
        textarea.setMaxWidth(800);
        gridPane.add(textarea, 0, 1, 2, 1);

        this.getChildren().add(gridPane);
    }
    

    public static void addText(String text) {
        textarea.appendText("> ");
        while (text.length() > 50) {
            textarea.appendText(text.substring(0, 50));
            text = text.substring(50);
            textarea.appendText("\n\r");
            textarea.setScrollTop(Double.MAX_VALUE);
        }
        textarea.appendText(text + "\r\n");
        textarea.setScrollTop(Double.MAX_VALUE);
    }

    public static void clearItems() {
        textarea.clear();
    }
}