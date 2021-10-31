package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.LoadingImageSupplier;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.tsp.TourBuilderV1;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.ModifyView;
import fr.insalyon.pldagile.view.menu.RequestItem;
import fr.insalyon.pldagile.view.menu.RequestView;
import fr.insalyon.pldagile.view.menu.SidePanel;
import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

public class Window {

    private static Stage mainStage = null;
    private Controller controller = null;
    private MapView mapView;
    private static SidePanel sidePanel;
    private static AnchorPane mainPanel = new AnchorPane();
    private final PointLayer pointLayer = new PointLayer(); // TODO Split point layers in 3 (one city map, one requests,
                                                            // one tour)
    private final LineLayer lineLayer = new LineLayer();
    private final int centeredZoomValue = 12;

    public Window(Controller controller) {
        this.controller = controller;
        pointLayer.setController(controller);
        this.controller.initWindow(this);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public void start(Stage stage) throws Exception {
        mainStage = stage;
        stage.setTitle("Picky - INSA Lyon");
        Image desktopIcon = new Image("/img/desktop-icon.png");
        stage.getIcons().add(desktopIcon);
        // cityMap = new CityMap();
        // planningRequest = new PlanningRequest();
        mapView = new MapView();
        mapView.addLayer(pointLayer); // Add the map layer
        mapView.addLayer(lineLayer); // Add the line (tour) layer
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

        mainPanel.getChildren().add(bp);
        loadSidePanel(false);

        Scene scene = new Scene(mainPanel, screenWidth, screenHeight);
        scene.getRoot().setStyle("-fx-font-family: 'Roboto'");
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
    private void loadSidePanel(boolean modifyMode){
        sidePanel = new SidePanel(controller);
        if (modifyMode){
            sidePanel.ModifyPanel();
        } else {
            sidePanel.MainSidePanel();
        }

        AnchorPane.setTopAnchor(sidePanel, 16D);
        AnchorPane.setBottomAnchor(sidePanel, 16D);
        AnchorPane.setRightAnchor(sidePanel, 16D);

        // Removing the existing side panel
        for (int i = 0; i < mainPanel.getChildren().size(); i++) {
            if (mainPanel.getChildren().get(i).getClass() == SidePanel.class){
                mainPanel.getChildren().remove(i);
                break;
            }
        }
        mainPanel.getChildren().add(sidePanel);
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

    public void renderMapAndRequests(CityMap cityMap, PlanningRequest planningRequest) {
        renderCityMap(cityMap);
        renderPlanningRequest(planningRequest);
    }

    public void renderCityMap(CityMap cityMap) {
        // Add all the intersections temporarily
        for (Map.Entry<Long, Intersection> entry : cityMap.getIntersections().entrySet()) {
            Intersection intersection = entry.getValue();
            MapPoint mapPoint = new MapPoint(intersection.getCoordinates().getLatitude(), intersection.getCoordinates().getLongitude());
            mapPoint.setId(intersection.getId());
            pointLayer.addPoint(mapPoint, new Circle(2, Colors.getMapIntersectionColor()));
        }
    }

    /**
     * Centers the map around the central coordinates of the city map sets the zoom
     * the level of a city in the map
     * 
     * @param cityMap
     */
    public void centerMap(CityMap cityMap) throws ExceptionXML {
        Coordinates coord = cityMap.getCenter();
        MapPoint mapCenter = new MapPoint(coord.getLatitude(), coord.getLongitude());
        // center the map around the calculated center coordinates
        mapView.setCenter(mapCenter);
        // sets the zoom at level 12: approximately the level of a city in our case
        mapView.setZoom(centeredZoomValue);
    }

    public void renderPlanningRequest(PlanningRequest planningRequest) {
        if (!planningRequest.getRequests().isEmpty() && planningRequest.getDepot() != null) {
            // Render the planning request
            Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
            MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
            depotPoint.setId(planningRequest.getDepot().getIntersection().getId());
            ArrayList<RequestItem> items = new ArrayList<>();
            planningRequest.getRequests().forEach(request -> {
                // Items in list
                Pickup pickup = request.getPickup();
                RequestItem pickupItem = new RequestItem("Pickup at " + request.getPickup().getIntersection().getId(), "Duration: " + request.getPickup().getDuration(), request.getId(), "Pickup",-1);
                Delivery delivery = request.getDelivery();
                RequestItem deliveryItem = new RequestItem("Delivery at " + request.getDelivery().getIntersection().getId(), "Duration: " + request.getDelivery().getDuration(), request.getId(), "Delivery",-1);
                items.add(pickupItem);
                items.add(deliveryItem);
                //Map points
                MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(pickup.getIntersection().getId());
                pointLayer.addPoint(
                        mapPoint,
                        IconProvider.getPickupIcon()
                );
                mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(delivery.getIntersection().getId());
                pointLayer.addPoint(
                        mapPoint,
                        IconProvider.getDropoffIcon()
                );
            });
            RequestView.setPickupItems(items);
            ModifyView.setPickupItems(items);
            pointLayer.addPoint(depotPoint, IconProvider.getDepotIcon());
            //pointLayer.addPoint(depotPoint, new ImageView("/img/depotPin/depot.png")); //TODO Scale it with zoom level
        }
    }

    public void renderTour(List<Intersection> intersections) {
        // TODO Update RequestView
        Intersection previousIntersection = intersections.get(0);
        for (Intersection intersection : intersections.subList(1, intersections.size())) {
            //Create line and add it
            MapPoint originPoint = new MapPoint(previousIntersection.getCoordinates().getLatitude(), previousIntersection.getCoordinates().getLongitude());
            MapPoint destinationPoint = new MapPoint(intersection.getCoordinates().getLatitude(), intersection.getCoordinates().getLongitude());
            pointLayer.addPoint(originPoint, new Circle(4, Colors.getTourIntersectionColor()));
            pointLayer.addPoint(destinationPoint, new Circle(4, Colors.getTourIntersectionColor()));
            lineLayer.addLine(new MapDestination(originPoint, destinationPoint), Colors.getTourIntersectionColor());
            //Update prev intersection
            previousIntersection = intersection;
        }
    }

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

    public void showInputAlert(String title, String header, String text){
        TextInputDialog dialog = new TextInputDialog("300");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            controller.confirm(result.get());
        } else {
            controller.cancel();
        }
    }

    public void clearRequest() {
        lineLayer.clearPoints();
        RequestView.clearItems();
    }

    public void clearTour() {
        lineLayer.clearPoints();
    }

    public void showModifyMenu() {
        loadSidePanel(true);
    }


    public void hideModifyMenu() {
        ModifyView.disableRowListener();
        pointLayer.disableMapIntersectionsListener();
        loadSidePanel(false);
    }

    public void disableEventListener() {
        ModifyView.disableRowListener();
        pointLayer.disableMapIntersectionsListener();
    }

    public void activeRowListener() {
        ModifyView.activeRowListener();
    }

    public void activeMapIntersectionsListener() {
        pointLayer.activeMapIntersectionsListener();
    }

    public void orderListRequests(ArrayList<Pair<Long, String>> steps, Map<Long, Request> requests, Depot depot) {
        ArrayList<RequestItem> items = new ArrayList<>();
        int index = 0;
        RequestItem item = new RequestItem("Depot at " + depot.getIntersection().getId(), "Departure time : " + depot.getDepartureTime(), -1, "Depot",0);
        items.add(item);
        for(Pair<Long, String> step : steps) {
            if(Objects.equals(step.getValue(), "pickup"))
            {
                item = new RequestItem("Pickup at " + requests.get(step.getKey()).getPickup().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getPickup().getDuration(), step.getKey(), "Pickup",index);
                items.add(item);
            }
            if(Objects.equals(step.getValue(), "delivery")){
                item = new RequestItem("Delivery at " + requests.get(step.getKey()).getDelivery().getIntersection().getId(), "Duration: " + requests.get(step.getKey()).getDelivery().getDuration(), step.getKey(),"Delivery",index);
                items.add(item);
            }
            index++;
        }
        item = new RequestItem("Depot at " + depot.getIntersection().getId(), "", -2,"Depot",(index-1));
        items.add(item);
        RequestView.clearItems();
        ModifyView.clearItems();
        RequestView.setPickupItems(items);
        ModifyView.setPickupItems(items);
    }

    public void addMapRequest(Request request) {
        Pickup pickup = request.getPickup();
        Delivery delivery = request.getDelivery();
        MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
        mapPoint.setId(pickup.getIntersection().getId());
        pointLayer.addPoint(
                mapPoint,
                new Circle(7, Color.RED)
        );
        mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
        mapPoint.setId(delivery.getIntersection().getId());
        pointLayer.addPoint(
                mapPoint,
                new Circle(7, Color.GREEN)
        );

    }
}
