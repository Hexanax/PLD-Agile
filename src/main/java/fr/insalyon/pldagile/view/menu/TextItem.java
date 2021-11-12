
package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.view.Fonts;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * TextItem is an item part of the LogView list {@link LogView}
 */
public class TextItem extends Region {
    private String value;
    private Label titleLabel;

    public TextItem(String value, String webColor) {
        this.value = value;

        GridPane mainGridPane = new GridPane();
        mainGridPane.setAlignment(Pos.BASELINE_LEFT);
        mainGridPane.setPadding(new Insets(0, 0, 0, 0));
        mainGridPane.setHgap(5);
        mainGridPane.setVgap(0);

        titleLabel = new Label("> " + value + "\r\n");
        titleLabel.setFont(Fonts.getBoldBodyFont());
        titleLabel.setTextFill(Color.web(webColor));
        mainGridPane.add(titleLabel, 0, 0, 4, 1);
        GridPane.setHalignment(titleLabel, HPos.LEFT);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 0, 0));

        this.getChildren().add(mainGridPane);
    }

    public void setFont(Font font) {
        titleLabel.setFont(font);
    }
}