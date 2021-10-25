package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class SidePanel extends Region {
    private Controller controller;
    private BorderPane mainBorderPane;


    public SidePanel(Controller controller) {
        this.controller = controller;
    }

    public void MainSidePanel(){
        ImportView importView = new ImportView(controller);
        RequestView pickupView = new RequestView(controller);
        mainBorderPane = new BorderPane();
        mainBorderPane.setTop(importView);
        mainBorderPane.setCenter(pickupView);
        this.getChildren().add(mainBorderPane);
    }

    public void ModifyPanel(){
        ModifyView modify = new ModifyView(controller);
        BorderPane mainBorderPane = new BorderPane();
        this.getChildren().add(mainBorderPane);
    }

}
