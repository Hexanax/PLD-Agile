package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.layout.*;

import java.awt.*;

public class SidePanel extends Region {
    private Controller controller;
    private GridPane mainBorderPane;

    public SidePanel(Controller controller) {
        this.controller = controller;
    }

    public void MainSidePanel() {
        ImportView importView = new ImportView(controller);
        RequestView requestView = new RequestView(controller);
        mainBorderPane = new GridPane();
        mainBorderPane.add(importView, 0, 0, 1, 1);
        mainBorderPane.add(requestView, 0, 1, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }

    public void ModifyPanel() {
        ModifyView modify = new ModifyView(controller);
        mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(modify);
        this.getChildren().add(mainBorderPane);

    }
}
