package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.view.Fonts;
import fr.insalyon.pldagile.view.View;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * This view is displayed on the lower left side of the window and is useful to
 * track the operations done by the user It also displays errors that can occur
 * with the usage so that the user can react accordingly and understand what's
 * going on under the hood
 */
public class LogView extends Region implements View {

    private static ListView<TextItem> listView;

    public LogView() {
    }


    /**
     * Render the LogView item
     */
    @Override
    public void render() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(8);
        gridPane.setVgap(8);

        // Title Label
        Label titleLabel = new Label("Messages");
        titleLabel.getStyleClass().add("h2");
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);

        // Logs display
        listView = new ListView<>();
        listView.getStyleClass().add("requests-list");
        listView.setPrefWidth(Double.POSITIVE_INFINITY);
        listView.setPrefHeight(75);
        gridPane.add(listView, 0, 1, 2, 1);

        this.getStyleClass().add("message-panel-section");
        this.getChildren().add(gridPane);
    }

    /**
     * Add a new line with the content and color demanded
     * @param text
     * @param color
     */
    public static void addText(String text, String color) {
        ObservableList<TextItem> textItems = listView.getItems();
        String formattedText = breakSpaces(text, 70);
        TextItem newLog = new TextItem(formattedText, color);
        textItems.add(newLog);
        textItems.get(Math.max(0, textItems.size() - 2)).setFont(Fonts.getBodyFont());
        listView.scrollTo(newLog);
    }

    /**
     * Add supplementary lines to the string if it's too long
     * @param str
     * @param maxChar the max character to not overcome
     * @return the reworked string
     */
    public static String breakSpaces(String str, int maxChar) {
        int counter = 0;
        StringBuilder newStr = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (counter++ > maxChar && c == ' ') {
                newStr.append('\n');
                counter = 0;
            }
            newStr.append(c);
        }
        return newStr.toString();
    }

}