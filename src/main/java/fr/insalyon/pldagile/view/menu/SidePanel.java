package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class SidePanel extends Region {
    private static final double HEIGHT_FACTOR = 0.9f;
    private static final double PANEL_WIDTH = 350d;

    private ScrollPane mainBorderPane = new ScrollPane();
    private final Controller controller;
    private final RequestView requestView;
    private final ImportView importView;


    public SidePanel(Controller controller) {
        this.controller=controller;
        this.importView = new ImportView();
        this.requestView = new RequestView();
    }

    public void mainSidePanel(ObservableList<AddressItem> list, int height) {
        this.requestView.setView(list);
        System.out.println(height);
        VBox vb = new VBox();
        vb.getChildren().add(importView);
        if (list.size() > 0) {
            vb.getChildren().add(requestView);
        }

        mainBorderPane.setContent(vb);
        VBox.setVgrow(mainBorderPane, Priority.ALWAYS);

        // Required to ensure the background is white
        vb.getStyleClass().add("side-panel-vbox");

        mainBorderPane.setVmax(height * HEIGHT_FACTOR);
        mainBorderPane.setMaxHeight(height * HEIGHT_FACTOR);
        mainBorderPane.setFitToWidth(true);
        mainBorderPane.setPrefWidth(PANEL_WIDTH);

        mainBorderPane.getStyleClass().add("side-panel");

        this.getChildren().clear();
        this.getChildren().add(mainBorderPane);
    }

    public RequestView getRequestView() {
        return requestView;
    }
}
