
package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextItem extends Region {
    private String value;
    private Label titleLabel;

    public TextItem(String value, String webColor){
        this.value = value;

        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_LEFT);
        maingp.setPadding(new Insets(0, 0, 0, 0));
        maingp.setHgap(5);
        maingp.setVgap(0);

        titleLabel = new Label("> " + value + "\r\n");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleLabel.setTextFill(Color.web(webColor));
        maingp.add(titleLabel, 0, 0, 4, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 0, 0));

        this.getChildren().add(maingp);
    }

    public void setFont(Font font) {
        titleLabel.setFont(font);
    }
}