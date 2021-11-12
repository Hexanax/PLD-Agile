package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.*;
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
//    private final RequestListView requestListView;
    private final SidePanelView sidePanel;
    private final LeftPanel leftPanel;

    private final int windowWidth = (int) (Screen.getPrimary().getBounds().getWidth() * 0.75);
    private final int windowHeight = (int) (Screen.getPrimary().getBounds().getHeight() * 0.75);

    /**
     * creates a window component
     * @param controller
     */
    public Window(Controller controller) {
        this.controller = controller;

        // Set the controllers inside the listener handlers
        MenuButtonListener.setController(controller);
        KeyboardListener.setController(controller);
        MouseListener.setController(controller);
        RequestMouseListener.setController(controller);

        this.cityMapView = new CityMapView(controller);
        this.requestMapView = new RequestMapView(controller);
        this.mapView = new MapView(controller);
        this.tourView = new TourView(controller);
        this.sidePanel = new SidePanelView(windowHeight, this, controller);
        this.leftPanel = new LeftPanel();

        requestMapView.setRequestListView(sidePanel.getRequestListView()); //TODO Check later
        sidePanel.getRequestListView().setRequestMapView(requestMapView); //TODO CHeck later

        // Get the view layers and add them to the map view
        mapView.addLayer(cityMapView.getLayer());
        mapView.addLayer(tourView.getTourLineLayer());
        mapView.addLayer(tourView.getTourPointLayer());
        mapView.addLayer(tourView.getTourDirectionLayer());
        mapView.addLayer(requestMapView.getLayer());
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * renders the {@link Stage} with the project's adequats information
     * and centers the map around France
     * @param stage
     */
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
        loadLeftPanel();

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

    /**
     * loads the {@link SidePanelView}
     */
    private void loadSidePanel() {
        sidePanel.render();
        AnchorPane.setTopAnchor(sidePanel, 16D);
        AnchorPane.setRightAnchor(sidePanel, 16D);

        // Removing the existing side panel
        for (int i = 0; i < mainPane.getChildren().size(); i++) {
            if (mainPane.getChildren().get(i).getClass() == SidePanelView.class) {
                mainPane.getChildren().remove(i);
                break;
            }
        }

        mainPane.getChildren().add(sidePanel);
    }

    /**
     * loads the {@link LeftPanel}
     */
    private void loadLeftPanel() {
        AnchorPane.setBottomAnchor(leftPanel, 16D);
        AnchorPane.setLeftAnchor(leftPanel, 16D);
        mainPane.getChildren().add(leftPanel);
    }

    /**
     * sets the header label to "Picky - INSA Lyon"
     * @return
     */
    private Label headerLabel() {
        final Label header = new Label("Picky - INSA Lyon");
        header.getStyleClass().add("header");
        return header;
    }

    /**
     * creates the {@link Group} copyright with the right ingo
     * @return
     */
    private Group createCopyright() {
        final Label copyright = new Label("Map data © OpenStreetMap contributors, CC-BY-SA.\n"
                + "Imagery  © OpenStreetMap, for non-commercial use only.");
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);
        return new Group(copyright);
    }

    /**
     * updates the map file's name when loading a file
     * @param fileName the loaded map file's name
     */
    public void updateMapFileName(String fileName) {
        ImportView.setImportMapLabel(fileName);
    }

    /**
     * updates the requests file's name when loading a file
     * @param fileName the loaded requests file's name
     */
    public void updateRequestFileName(String fileName) {
        ImportView.setImportRequestLabel(fileName);
    }

    /**
     * displays an alert of type CONFIRMATION, that will guide the {@link Controller} in confirming
     * or cancelling the action
     * @param title the alert's title
     * @param header the alert's header
     * @param text the alert's text
     */
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

    /**
     * display the alert of type CONFIRMATION to ask the user if he wants to
     * Continue looking for a more optimized path or display the one already found
     * This occurs when the user clicks on "Compute Slowly" button
     * @return
     */
    public boolean continueTourCompute() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Heavy computing task detected");
        alert.setHeaderText("Press OK to search for a more optimized way or cancel to display the current computed tour");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Adds the state text in the {@link LogView}
     * @param message the state's message
     */
    public void addStateFollow(String message) {
        LogView.addText(message, "green");
    }

    /**
     * Adds state's warning in the {@link LogView}
     * @param message the state's message
     */
    public void addWarningStateFollow(String message) {
        LogView.addText(message, "red");
    }

    /**
     * Deletes the view by hiding the tour
     */
    public void deleteView() {
        hideTour();
    }

    /**
     * highlights the {@link CityMapView} intersections and renders the request list of {@link SidePanelView}
     */
    public void addView() {
        hideTour();
        cityMapView.highlight();
        sidePanel.getRequestListView().renderUnordered();
    }

    /**
     * Sets the main view by showing the {@link TourView} and showing the city map
     * it also unhighlights the map's intersections
     */
    public void mainView() {
        tourView.show();
        cityMapView.unHighlight();
        showCityMap();
        renderOrderedList();
    }

    /**
     * hides the city map by first unhghlighting its intersections
     */
    public void hideCityMap() {
        cityMapView.unHighlight();
        cityMapView.hide();
    }

    /**
     * displays the city map
     */
    public void showCityMap() {
        cityMapView.show();
    }

    /**
     * Renders the ordered list of requests in the {@link SidePanelView}
     */
    public void renderOrderedList() {
        sidePanel.getRequestListView().renderOrdered();
    }

    /**
     * Hides the tour {@link TourView}
     */
    public void hideTour() {
        tourView.hide();
    }


    /**
     * Makes the duration field of the item's request (that has the same id as the one
     * in entry) editable inside the {@link SidePanelView}
     * @param editable boolean to indicate if we can edit or not
     * @param id request's ID
     */
    public void makeLastRequestAddedEditable(boolean editable, long id) {
        sidePanel.getRequestListView().makeLastRequestAddedEditable(editable, id);
    }

    /**
     * Gets the text input of the request duration field
     * @return
     */
    public String[] getEditableRequestDuration() {
        return sidePanel.getRequestListView().getEditableDuration();
    }

    /**
     * Highlights the request's icon that has the same id as the one in entry accrording to its type
     * in the {@link RequestMapView}
     * @param id request's id
     * @param type request's type (delivery or pickup)
     */
    public void highlightAddress(long id, String type) {
        requestMapView.scaleUpAddress(id, type);
    }

    /**
     * Unhighlights the request's icon that has the same id as the one in entry in
     * the {@link RequestMapView}
     * @param id request's id
     */
    public void unHighlightAddress(long id) {
        requestMapView.unScaleUpAddresses(id);
    }
}
