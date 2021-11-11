package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class BottomPanel extends Region {

    private final GridPane mainBorderPane;
    private final LogView logView;

    public BottomPanel(){
        logView = new LogView();
        logView.render();
        mainBorderPane = new GridPane();
        mainBorderPane.add(logView, 0, 0, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }
}
