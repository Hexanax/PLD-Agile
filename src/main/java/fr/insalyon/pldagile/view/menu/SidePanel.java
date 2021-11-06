package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;

public class SidePanel extends Region {
    private GridPane mainBorderPane;

    public SidePanel() {

    }

    public void MainSidePanel(ObservableList<AddressItem> list) {
        ImportView importView = new ImportView();
        RequestView requestView = new RequestView();
        requestView.setView(list);
        mainBorderPane = new GridPane();
        mainBorderPane.add(importView, 0, 0, 1, 1);
        mainBorderPane.add(requestView, 0, 1, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }

}
