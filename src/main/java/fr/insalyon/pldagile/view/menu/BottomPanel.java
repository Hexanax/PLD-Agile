package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class BottomPanel extends Region {
    private GridPane mainBorderPane;
    private LogView logView;

    public BottomPanel(){
        this.logView = new LogView();
        this.mainBorderPane = new GridPane();
        mainBorderPane.add(logView, 0, 0, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }
}
