package fr.insalyon.pldagile.view.menu;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * The left panel wraps all the components and views on the left side window into a coherent set
 */
public class LeftPanel extends Region {

    /**
     * Constructor automatically rendering the left panel
     */
    public LeftPanel() {
        LogView logView = new LogView();
        logView.render();
        GridPane mainBorderPane = new GridPane();
        mainBorderPane.add(logView, 0, 0, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }
}
