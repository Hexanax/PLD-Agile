package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.View;
import fr.insalyon.pldagile.view.Window;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

/**
 * Side panel
 */
public class SidePanelView extends Region implements View {
    private static final double HEIGHT_FACTOR = 0.9f;
    private static final double PANEL_WIDTH = 350d;

    private final Controller controller;
    private final ScrollPane mainBorderPane = new ScrollPane();
    private final RequestManagerView requestView;
    private final RequestListView requestListView;
    private final ImportView importView;
    private final double windowHeight;
    private VBox verticalBox;

    public SidePanelView(double windowHeight, Window window, Controller controller) {
        this.controller = controller;
        this.importView = new ImportView();
        this.requestView = new RequestManagerView(controller);
        this.requestListView = new RequestListView(requestView.getGridPane(), controller);

        //Update call back to consistently check whether we should add the request view or not depending on addresses being present or none
        requestListView.addUpdateCallback(() -> {
            System.out.println("Update callback sidepannel to request list view");
            if (requestListView.getAddressItems().size() > 0) {
               if(!verticalBox.getChildren().contains(requestView)) {
                    verticalBox.getChildren().add(requestView);
                }
            }
        });

        this.windowHeight = windowHeight;
        render();
    }

    public RequestManagerView getRequestView() {
        return requestView;
    }

    public RequestListView getRequestListView() {
        return requestListView;
    }

    @Override
    public void render() {
        verticalBox = new VBox();
        verticalBox.getChildren().add(importView);
//        System.out.println("Side panel try to show request view show size = " + requestListView.getAddressItems().size());
//        if (requestListView.getAddressItems().size() > 0) {
//            verticalBox.getChildren().add(requestView);
//        }

        mainBorderPane.setContent(verticalBox);
        VBox.setVgrow(mainBorderPane, Priority.ALWAYS);

        // Required to ensure the background is white
        verticalBox.getStyleClass().add("side-panel-vbox");

        mainBorderPane.setVmax(windowHeight * HEIGHT_FACTOR);
        mainBorderPane.setMaxHeight(windowHeight * HEIGHT_FACTOR);
        mainBorderPane.setFitToWidth(true);
        mainBorderPane.setPrefWidth(PANEL_WIDTH);

        mainBorderPane.getStyleClass().add("side-panel");

        this.getChildren().clear();
        this.getChildren().add(mainBorderPane);
    }

}
