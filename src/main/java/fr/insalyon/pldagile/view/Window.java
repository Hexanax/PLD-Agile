package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.LoadingImageSupplier;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.RequestType;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class Window {

    private static Stage mainStage = null;
    private Controller controller;
    private final AnchorPane mainPane = new AnchorPane();
    private final MapView mapView;
    private final CityMapView cityMapView;
    private final RequestMapView requestMapView;
    private final TourView tourView;
    private final RequestListView requestListView;
    private final SidePanel sidePanel;
    private final BottomPanel bottomPanel;
    private boolean selectingIntersection;

    private ButtonListener buttonListener;
    private KeyboardListener keyboardListener;
    private MouseListener mouseListener;

    public Window(Controller controller) {
        this.controller = controller;

        buttonListener = new ButtonListener(controller);
        keyboardListener = new KeyboardListener(controller);
        mouseListener = new MouseListener(controller);

        this.cityMapView = new CityMapView(controller);
        this.requestMapView = new RequestMapView(controller);
        this.mapView = new MapView(controller);
        this.tourView = new TourView(controller);
        this.requestListView = new RequestListView(controller);
        this.sidePanel = new SidePanel(controller);
        this.bottomPanel = new BottomPanel(controller);

        //Get the view layers and add them to the map view
        mapView.addLayer(cityMapView.getLayer());
        mapView.addLayer(tourView.getTourLineLayer());
        mapView.addLayer(tourView.getTourPointLayer());
        mapView.addLayer(tourView.getTourDirectionLayer());
        mapView.addLayer(requestMapView.getLayer());
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public RequestMapView getRequestMapView() {
        return requestMapView;
    }

    public CityMapView getCityMapView() {
        return cityMapView;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public void render(Stage stage) throws Exception {
        mainStage = stage;
        stage.setTitle("Picky - INSA Lyon");
        Image desktopIcon = new Image("/img/desktop-icon.png");
        stage.getIcons().add(desktopIcon);
        // cityMap = new CityMap();
        // planningRequest = new PlanningRequest();
        //mapView.addLayer(pointLayer); // Add the map layer
        //mapView.addLayer(lineLayer); // Add the line (tour) layer
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
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

        Scene scene = new Scene(mainPane, screenWidth, screenHeight);
        scene.getRoot().setStyle("-fx-font-family: 'Roboto'");
        scene.setOnKeyPressed(KeyboardListener::keyPressed);
        scene.setOnMouseClicked(MouseListener::mouseClicked);

        bp.getChildren().addAll(mapView, headerLabel, copyright);
        headerLabel.setManaged(false);
        headerLabel.setVisible(false);
        scene.getStylesheets().add("/style.css");
        stage.setScene(scene);
        stage.setFullScreen(false);
        MapPoint mapCenter = new MapPoint(46.75, 2.80);
        mapView.setCenter(mapCenter);
        mapView.setZoom(7);
        LoadingImageSupplier loadingImageSupplier = new LoadingImageSupplier();
        MapView.setPlaceholderImageSupplier(loadingImageSupplier);
        stage.show();
    }

    private void loadSidePanel() {
        sidePanel.MainSidePanel(this.requestListView.getAddressItems());
        AnchorPane.setTopAnchor(sidePanel, 16D);
        AnchorPane.setBottomAnchor(sidePanel, 16D);
        AnchorPane.setRightAnchor(sidePanel, 16D);

        // Removing the existing side panel
        for (int i = 0; i < mainPane.getChildren().size(); i++) {
            if (mainPane.getChildren().get(i).getClass() == SidePanel.class) {
                mainPane.getChildren().remove(i);
                break;
            }
        }

        mainPane.getChildren().add(sidePanel);
    }

    private void loadBottomPanel() {
        AnchorPane.setTopAnchor(bottomPanel, 650D);
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

    public void addStateFollow(String message) {
        TextItem item = new TextItem(message, "#000000");
        LogView.addTextItem(item);
    }

    public void addWarningStateFollow(String message) {
        TextItem item = new TextItem(message, "#FF0000");
        LogView.addTextItem(item);
    }

    public void setSelectingIntersection(boolean selectingIntersection) {
        this.selectingIntersection = selectingIntersection;
        if(selectingIntersection) {
            cityMapView.setIntersectionColor(Colors.getMapIntersectionSelectColor());
        } else {
            cityMapView.setIntersectionColor(Colors.getMapIntersectionColor());
        }
    }

    public boolean isSelectingIntersection(boolean selecting) {
        return selectingIntersection;
    }

    /**
     * Highlights request pins on click in request list
     */
    private EventHandler<MouseEvent> onRequestListItemClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() == 1) {
                System.out.println("MouseEvent" + event);
                if (sidePanel.getRequestView().getRequestList().getSelectionModel().getSelectedItem() != null) {
                    System.out.println("condition");
                    //requestMapView.getPlanningRequestPoints().highlightIcon(sidePanel.getRequestView().getRequestList().getSelectionModel().getSelectedItem().getRequestNumber()); //TODO Reimplement
                }

            }
        }
    };

    //TODO : show in list selected request when selecting a pin on the map
    private EventHandler<MouseEvent> onRequestPinClick = event -> {
        if (event.getClickCount() == 1) {
            System.out.println("MouseEvent" + event);
            RequestMapPin rmp = (RequestMapPin) event.getTarget();
            Long requestId = rmp.getRequestId();
            RequestType type = rmp.getType();

        }
    };

    public void activeItemListener() {
        sidePanel.getRequestView().getRequestList().addEventHandler(MouseEvent.MOUSE_CLICKED, onRequestListItemClick);
        requestMapView.getPlanningRequestPoints().addEventHandler(MouseEvent.MOUSE_CLICKED, onRequestPinClick);
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
        tourView.show();
        cityMapView.unHighlight();
        showCityMap();
        renderOrderedList();
    }

    public void hideCityMap() {
        cityMapView.unHighlight();
        cityMapView.hide();
    }

    public void showCityMap(){
        cityMapView.show();
    }

    public void renderOrderedList(){
        requestListView.renderOrdered();
    }

    public void hideTour() {
        tourView.hide();
    }

    public void makeLastRequestAddedEditable(boolean editable, long id) {
        requestListView.makeLastRequestAddedEditable(editable, id);
    }

    public String[] getEditableRequestDuration(){
        return requestListView.getEditableDuration();
    }
}
