package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;

public class SidePanel extends Region {
    private GridPane mainBorderPane;
    private final Controller controller;
    private final RequestView requestView;
    private final ImportView importView;


    public SidePanel(Controller controller) {
        this.controller=controller;
        this.importView = new ImportView();
        this.requestView = new RequestView(controller);
    }

    public void MainSidePanel(ObservableList<AddressItem> list) {
        this.requestView.setView(list);
        mainBorderPane = new GridPane();
        mainBorderPane.add(importView, 0, 0, 1, 1);
        mainBorderPane.add(requestView, 0, 1, 1, 1);
        mainBorderPane.getStyleClass().add("side-panel");
        this.getChildren().add(mainBorderPane);
    }

    public RequestView getRequestView() {
        return requestView;
    }
}
