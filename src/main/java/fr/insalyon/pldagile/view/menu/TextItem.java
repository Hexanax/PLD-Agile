package fr.insalyon.pldagile.view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextItem extends Region {
    private String value;

    public TextItem(String value){
        this.value = value;

        GridPane maingp = new GridPane();
        maingp.setAlignment(Pos.BASELINE_CENTER);
        maingp.setPadding(new Insets(5, 5, 5, 5));
        maingp.setHgap(5);
        maingp.setVgap(5);

        Label titleLabel = new Label(value);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        maingp.add(titleLabel, 0, 0, 4, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);
        GridPane.setMargin(titleLabel, new Insets(10, 0, 5, 0));

        this.getChildren().add(maingp);
    }
}