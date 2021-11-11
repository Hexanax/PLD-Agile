package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.LoadingImageSupplier;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class Window {

    private static Stage mainStage = null;
    private final Controller controller;
    private final AnchorPane mainPane = new AnchorPane();
    private final MapView mapView;
    private final CityMapView cityMapView;
    private final RequestMapView requestMapView;
    private final TourView tourView;
    private final RequestListView requestListView;
    private final SidePanel sidePanel;
    private final BottomPanel bottomPanel;

    private int windowWidth = (int) (Screen.getPrimary().getBounds().getWidth() * 0.75);
    private int windowHeight = (int) (Screen.getPrimary().getBounds().getHeight() * 0.75);

    public Window(Controller controller) {
        this.controller = controller;

        // Set the controllers inside the listener handlers
        ButtonListener.setController(controller);
        KeyboardListener.setController(controller);
        MouseListener.setController(controller);

        this.cityMapView = new CityMapView(controller);
        this.requestMapView = new RequestMapView(controller);
        this.mapView = new MapView(controller);
        this.tourView = new TourView(controller);
        this.requestListView = new RequestListView(controller);
        this.sidePanel = new SidePanel(controller);
        this.bottomPanel = new BottomPanel();

        requestMapView.setRequestListView(requestListView);
        requestListView.setRequestMapView(requestMapView);

        // Get the view layers and add them to the map view
        mapView.addLayer(cityMapView.getLayer());
        mapView.addLayer(tourView.getTourLineLayer());
        mapView.addLayer(tourView.getTourPointLayer());
        mapView.addLayer(tourView.getTourDirectionLayer());
        mapView.addLayer(requestMapView.getLayer());

        this.requestListView.getAddressItems().addListener(new ListChangeListener<AddressItem>() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                loadSidePanel();
            }
        });
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public void render(Stage stage) {
        mainStage = stage;
        // Title and icon
        stage.setTitle("Picky - INSA Lyon");
        Image desktopIcon = new Image("/img/desktop-icon.png");
        stage.getIcons().add(desktopIcon);
        // Window dimensions
        mapView.setZoom(3);

        final Label headerLabel = headerLabel();
        final Group copyright = createCopyright();
        StackPane bp = new StackPane() {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                headerLabel.setLayoutY(0.0);
                copyright.setLayoutX(getWidth() - copyright.prefWidth(-1));
                copyright.setLayoutY(getHeight() - copyright.prefHeight(-1));
            }
        };

        AnchorPane.setTopAnchor(bp, 0D);
        AnchorPane.setBottomAnchor(bp, 0D);
        AnchorPane.setLeftAnchor(bp, 0D);
        AnchorPane.setRightAnchor(bp, 0D);

        mainPane.getChildren().add(bp);
        loadSidePanel();
        loadBottomPanel();

        Scene scene = new Scene(mainPane, windowWidth, windowHeight);
        scene.getRoot().setStyle("-fx-font-family: 'Roboto'");
        scene.setOnKeyPressed(KeyboardListener::keyPressed);
        scene.setOnMouseClicked(MouseListener::mouseClicked);

        bp.getChildren().addAll(mapView, headerLabel, copyright);
        headerLabel.setManaged(false);
        headerLabel.setVisible(false);
        scene.getStylesheets().add("/style.css");
        stage.setScene(scene);
        stage.setFullScreen(false);

        // Center the map on France
        MapPoint mapCenter = new MapPoint(46.75, 2.80);
        mapView.setCenter(mapCenter);
        mapView.setZoom(7);
        // Imagine placeholder when the map tiles are loading from openstreetmap
        LoadingImageSupplier loadingImageSupplier = new LoadingImageSupplier();
        MapView.setPlaceholderImageSupplier(loadingImageSupplier);
        stage.show();
    }

    private void loadSidePanel() {
        sidePanel.mainSidePanel(this.requestListView.getAddressItems(), windowHeight);
        AnchorPane.setTopAnchor(sidePanel, 16D);
        AnchorPane.setRightAnchor(sidePanel, 16D);

        // Removing the existing side panel
        for (int i = 0; i < mainPane.getChildren().size(); i++) {
            if (mainPane.getChildren().get(i).getClass() == SidePanel.class) {
                mainPane.getChildren().remove(i);
                break;
            }
        }

        requestListView.setRequestView(sidePanel.getRequestView());
        mainPane.getChildren().add(sidePanel);
    }

    private void loadBottomPanel() {
        AnchorPane.setBottomAnchor(bottomPanel, 16D);
        AnchorPane.setLeftAnchor(bottomPanel, 16D);
        mainPane.getChildren().add(bottomPanel);
    }

    private Label headerLabel() {
        final Label header = new Label("Picky - INSA Lyon");
        header.getStyleClass().add("header");
        return header;
    }

    private Group createCopyright() {
        final Label copyright = new Label("Map data © OpenStreetMap contributors, CC-BY-SA.\n"
                + "Imagery  © OpenStreetMap, for non-commercial use only.");
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);
        return new Group(copyright);
    }

    public void updateMapFileName(String fileName) {
        ImportView.setImportMapLabel(fileName);
    }

    public void updateRequestFileName(String fileName) {
        ImportView.setImportRequestLabel(fileName);
    }

    public void showValidationAlert(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            controller.cancel();
        } else {
            controller.confirm();
        }
    }

    public boolean continueTourCompute() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("title");
        alert.setHeaderText("header");
        alert.setContentText("text");

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return false;
        } else {
            return true;
        }
    }

    public void addStateFollow(String message) {
        LogView.addText(message, "green");
    }

    public void addWarningStateFollow(String message) {
        LogView.addText(message, "red");
    }

    public void deleteView() {
        hideTour();
    }

    public void addView() {
        hideTour();
        cityMapView.highlight();
        requestListView.renderUnordered();
    }

    public void mainView() {
        // System.out.println("Main view called");
        tourView.show();
        cityMapView.unHighlight();
        showCityMap();
        renderOrderedList();
    }

    public void hideCityMap() {
        cityMapView.unHighlight();
        cityMapView.hide();
    }

    public void showCityMap() {
        cityMapView.show();
    }

    public void renderOrderedList() {
        requestListView.renderOrdered();
    }

    public void hideTour() {
        tourView.hide();
    }

    public void makeLastRequestAddedEditable(boolean editable, long id) {
        requestListView.makeLastRequestAddedEditable(editable, id);
    }

    public String[] getEditableRequestDuration() {
        return requestListView.getEditableDuration();
    }

    public void highlightAddress(long id, String type) {
        requestMapView.scaleUpAddress(id, type);
    }

    public void unHighlightAddress(long id) {
        requestMapView.unScaleUpAddresses(id);
    }
}
