package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

    @Override
    public void render() {
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

        // Logs display
        listView = new ListView<>();
        listView.getStyleClass().add("requests-list");
        listView.setMaxHeight(150);
        listView.setPrefWidth(200);
        gridPane.add(listView, 0, 1, 2, 1);

        this.getChildren().add(gridPane);
    }

    public static void addText(String text, String color) {
        ObservableList<TextItem> textItems = listView.getItems();
        String formattedText = breakSpaces(text, 35);
        TextItem newLog = new TextItem(formattedText, color);
        textItems.add(newLog);
        textItems.get(Math.max(0, textItems.size() - 2)).setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        listView.scrollTo(newLog);
    }

    public static String breakSpaces(String str, int maxChar) {
        int counter = 0;
        StringBuilder newStr = new StringBuilder();
        for (char c : str.toCharArray()) {
            if(counter++ > 35 && c == ' ') {
                newStr.append('\n');
                counter = 0;
            }
            newStr.append(c);
        }
        return newStr.toString();
    }

}