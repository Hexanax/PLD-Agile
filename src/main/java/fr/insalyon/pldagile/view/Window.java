package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.LoadingImageSupplier;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.*;
import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class Window {

    private static Stage mainStage = null;
    private Controller controller;
    private final AnchorPane mainPane = new AnchorPane();
    private final PointLayer pointLayer = new PointLayer(); // TODO Split point layers in 3 (one city map, one requests, one tour)
    private final LineLayer lineLayer = new LineLayer();
    private final MapView mapView;
    private final CityMapView cityMapView;
    private final RequestMapView requestMapView;
    private final TourView tourView;
    private final RequestListView requestListView;
    private final SidePanel sidePanel;
    private final BottomPanel bottomPanel;




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

        mapView.addLayer(cityMapView.getLayer());
        mapView.addLayer(requestMapView.getLayer());
        mapView.addLayer(tourView.getTourLineLayer());
        mapView.addLayer(tourView.getTourPointLayer());
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
        loadSidePanel(this.sidePanel);
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

    private void loadSidePanel(SidePanel sidePanel) {
        sidePanel.MainSidePanel(this.requestListView.getList());
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

    private void loadBottomPanel(){
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

    public void clearMap() {
        pointLayer.clearPoints();
    }

//    public void renderMapAndRequests(CityMap cityMap, PlanningRequest planningRequest) {
//        renderCityMap(cityMap);
//        renderPlanningRequest(planningRequest);
//    }

    public void updateMapFileName(String fileName) {
        ImportView.setImportMapLabel(fileName);
    }

    public void updateRequestFileName(String fileName) {
        ImportView.setImportRequestLabel(fileName);
    }

//    /**
//     * Centers the map around the central coordinates of the city map sets the zoom
//     * the level of a city in the map
//     *
//     * @param cityMap
//     */
//    public void centerMap(CityMap cityMap) throws ExceptionXML {
//        Coordinates coordinates = cityMap.getCenter();
//        MapPoint mapCenter = new MapPoint(coordinates.getLatitude(), coordinates.getLongitude());
//        // center the map around the calculated center coordinates
//        mapView.setCenter(mapCenter);
//        // sets the zoom at level 12: approximately the level of a city in our case
//        int centeredZoomValue = 12;
//        mapView.setZoom(centeredZoomValue);
//    }

   /* public void renderPlanningRequest(PlanningRequest planningRequest) {
        if (!planningRequest.getRequests().isEmpty() && planningRequest.getDepot() != null) {
            // Render the planning request
            Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
            MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
            depotPoint.setId(planningRequest.getDepot().getIntersection().getId());
            ArrayList<RequestItem> items = new ArrayList<>();
            planningRequest.getRequests().forEach(request -> {
                // Items in list
                Pickup pickup = request.getPickup();
                RequestItem pickupItem = new RequestItem("Pickup at " + request.getPickup().getIntersection().getId(), "Duration: " + request.getPickup().getDuration(), request.getId(), "Pickup", -1);
                Delivery delivery = request.getDelivery();
                RequestItem deliveryItem = new RequestItem("Delivery at " + request.getDelivery().getIntersection().getId(), "Duration: " + request.getDelivery().getDuration(), request.getId(), "Delivery", -1);
                items.add(pickupItem);
                items.add(deliveryItem);
                //Map points
                MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(pickup.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                pointLayer.addRequestPoint(
                        mapPoint,
                        IconProvider.getPickupIcon()
                );
                mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(delivery.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                pointLayer.addRequestPoint(
                        mapPoint,
                        IconProvider.getDropoffIcon()
                );
            });
            RequestMenuView.setPickupItems(items);

            pointLayer.addPoint(depotPoint, new Circle(7, Color.ORANGE));
            //TODO Scale it with zoom level
        }
    }*/

//    public void renderTour(Tour tour) {
//        // TODO Update RequestView
//        Intersection previousIntersection = tour.getDepot().getIntersection();
//        for (Segment segment : tour.getPath()) {
//            Intersection destinationIntersection = segment.getDestination();
//            MapPoint originPoint = new MapPoint(previousIntersection.getCoordinates().getLatitude(), previousIntersection.getCoordinates().getLongitude());
//            MapPoint destinationPoint = new MapPoint(destinationIntersection.getCoordinates().getLatitude(), destinationIntersection.getCoordinates().getLongitude());
//            MapDestination mapDestination = new MapDestination(originPoint, destinationPoint);
//            pointLayer.addPoint(originPoint, new Circle(4, Colors.getTourIntersectionColor()));
//            pointLayer.addPoint(destinationPoint, new Circle(4, Colors.getTourIntersectionColor()));
//            lineLayer.addLine(mapDestination, Colors.getTourIntersectionColor());
//            //Update prev intersection
//            previousIntersection = destinationIntersection;
//        }
//    }

    public void showWarningAlert(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
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
            controller.confirm("");
        }
    }

    public void showInputAlert(String title, String header, String text) {
        TextInputDialog dialog = new TextInputDialog("300");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            controller.confirm(result.get());
        } else {
            controller.cancel();
        }
    }



    public void clearTour() {
        lineLayer.clearPoints();
    }

    public void showModifyMenu() {
        //loadSidePanel(true);
    }

    public void addStateFollow(String message) {
        TextItem item = new TextItem(message, "#000000");
        LogView.addTextItem(item);
    }

    public void addWarningStateFollow(String message) {
        TextItem item = new TextItem(message, "#FF0000");
        LogView.addTextItem(item);
    }


    //TODO Update modify view
//    public void hideModifyMenu() {
//        RequestMenuView.disableRowListener();
//        pointLayer.disableMapIntersectionsListener();
//        loadSidePanel(false);
//    }
//
//    public void disableEventListener() {
//        RequestMenuView.disableRowListener();
//        pointLayer.disableMapIntersectionsListener();
//        pointLayer.disableRequestIntersectionsListener();
//    }
//
//    public void activeRowListener() {
//        RequestMenuView.activeRowListener();
//    }

   /* public void orderListRequests(ArrayList<Pair<Long, String>> steps, Map<Long, Request> requests, Depot depot) {
        pointLayer.clearRequestPoints();
        ArrayList<RequestItem> items = new ArrayList<>();
        int index = 0;
        RequestItem item = new RequestItem("Depot at " + depot.getIntersection().getId(), "Departure time : " + depot.getDepartureTime(), -1, "Depot", 0);
        items.add(item);
        for (Pair<Long, String> step : steps) {
            if (Objects.equals(step.getValue(), "pickup")) {
                item = new RequestItem("Pickup at " + requests.get(step.getKey()).getPickup().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getPickup().getDuration(), step.getKey(), "Pickup", index);
                items.add(item);
                double mapPointLatitude = requests.get(step.getKey()).getPickup().getIntersection().getCoordinates().getLatitude();
                double mapPointLongitude = requests.get(step.getKey()).getPickup().getIntersection().getCoordinates().getLongitude();
                MapPoint mapPoint = new MapPoint(mapPointLatitude, mapPointLongitude);
                mapPoint.setId(requests.get(step.getKey()).getPickup().getIntersection().getId());
                mapPoint.setRequestId(requests.get(step.getKey()).getId());
                mapPoint.setStepIndex(index);
                pointLayer.addRequestPoint(
                        mapPoint,
                        IconProvider.getPickupIcon()
                );
            }
            if (Objects.equals(step.getValue(), "delivery")) {
                item = new RequestItem("Delivery at " + requests.get(step.getKey()).getDelivery().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getDelivery().getDuration(), step.getKey(), "Delivery", index);
                items.add(item);
                double mapPointLatitude = requests.get(step.getKey()).getDelivery().getIntersection().getCoordinates().getLatitude();
                double mapPointLongitude = requests.get(step.getKey()).getDelivery().getIntersection().getCoordinates().getLongitude();
                MapPoint mapPoint = new MapPoint(mapPointLatitude, mapPointLongitude);
                mapPoint.setId(requests.get(step.getKey()).getDelivery().getIntersection().getId());
                mapPoint.setRequestId(requests.get(step.getKey()).getId());
                mapPoint.setStepIndex(index);
                pointLayer.addRequestPoint(
                        mapPoint,
                        IconProvider.getDropoffIcon()
                );
            }

            index++;
        }
        item = new RequestItem("Depot at " + depot.getIntersection().getId(), "", -2, "Depot", (index - 1));
        items.add(item);
        RequestMenuView.clearItems();

        RequestMenuView.setPickupItems(items);

    }*/

    /*public void addMapRequest(Request request) {
        Pickup pickup = request.getPickup();
        Delivery delivery = request.getDelivery();
        MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
        mapPoint.setId(pickup.getIntersection().getId());
        mapPoint.setRequestId(request.getId());
        pointLayer.addPoint(
                mapPoint,
                IconProvider.getPickupIcon()
        );
        mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
        mapPoint.setId(delivery.getIntersection().getId());
        mapPoint.setRequestId(request.getId());
        pointLayer.addPoint(
                mapPoint,
                IconProvider.getDropoffIcon()
        );
    }*/

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        String propertyName = evt.getPropertyName();
//        //System.out.println(propertyName);
//        if(propertyName.equals("cityMapUpdate")) {
//            CityMap newCityMapValue = (CityMap) evt.getNewValue();
//            clearMap();
//            clearRequest();
//            clearTour();
//            renderCityMap(newCityMapValue);
//            try {
//                centerMap(newCityMapValue);
//            } catch (ExceptionXML e) {
//                e.printStackTrace();
//            }
//        } else if (propertyName.equals("planningRequestUpdate")){
//            clearRequest();
//            clearTour();
//            PlanningRequest newPlanningRequestValue = (PlanningRequest) evt.getNewValue();
//            renderPlanningRequest(newPlanningRequestValue);
//
//        } else if (propertyName.equals("tourUpdate")){
//            Tour newTourValue = (Tour) evt.getNewValue();
//            clearTour();
//            orderListRequests(newTourValue.getSteps(), newTourValue.getRequests(), newTourValue.getDepot());
//            renderTour(newTourValue);
//        }
//    }
}
