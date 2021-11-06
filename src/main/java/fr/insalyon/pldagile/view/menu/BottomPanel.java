package fr.insalyon.pldagile.view.menu;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class BottomPanel extends Region {
    private GridPane mainBorderPane;

    public BottomPanel(){
        LogView logView = new LogView();
        mainBorderPane = new GridPane();
        mainBorderPane.add(logView, 0, 0, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }
}
