package fr.insalyon.pldagile;

import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.MapPoint;
import fr.insalyon.pldagile.view.maps.MapView;
import fr.insalyon.pldagile.view.maps.PointLayer;
import fr.insalyon.pldagile.view.menu.RequestItem;
import fr.insalyon.pldagile.view.menu.RequestView;
import fr.insalyon.pldagile.view.menu.SidePanel;
import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class PickyApplication extends Application {

    private static Stage mainStage = null;

    private static PlanningRequest planningRequest;
    private static CityMap cityMap;
    private static MapView mapView;
    private static final PointLayer pointLayer = new PointLayer();

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void start(Stage stage) throws ExceptionXML {
        mainStage = stage;
        stage.setTitle("Picky - INSA Lyon");
        Image desktopIcon = new Image("/img/desktop-icon.png");
        stage.getIcons().add(desktopIcon);
        cityMap = new CityMap();
        planningRequest = new PlanningRequest();
        mapView = new MapView();
        mapView.addLayer(pointLayer); //Add the map layer
        SidePanel sidePanel = new SidePanel();
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
        BorderPane mainPanel = new BorderPane();
        mainPanel.setCenter(bp);
        mainPanel.setRight(sidePanel);
        Scene scene = new Scene(mainPanel, screenWidth, screenHeight);
        scene.getRoot().setStyle("-fx-font-family: 'Roboto'");
        bp.getChildren().addAll(mapView, headerLabel, copyright);
        headerLabel.setManaged(false);
        headerLabel.setVisible(false);
        scene.getStylesheets().add("/style.css");
        stage.setScene(scene);
        stage.setFullScreen(true);
        MapPoint mapCenter = new MapPoint(46.75, 2.80);
        mapView.setCenter(mapCenter);
        mapView.setZoom(7);
        LoadingImageSupplier loadingImageSupplier = new LoadingImageSupplier();
        MapView.setPlaceholderImageSupplier(loadingImageSupplier);
        stage.show();
    }

    private Label headerLabel() {
        final Label header = new Label("Picky - INSA Lyon");
        header.getStyleClass().add("header");
        return header;
    }

    private Group createCopyright() {
        final Label copyright = new Label(
                "Map data © OpenStreetMap contributors, CC-BY-SA.\n" +
                        "Imagery  © OpenStreetMap, for non-commercial use only."
        );
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);
        return new Group(copyright);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void emptyCityMap() {
        cityMap.getIntersections().clear();
        cityMap.getSegments().clear();
    }

    public static void clearMap() {
        pointLayer.clearPoints();
    }

    public static void emptyPlanningRequest() {
        planningRequest.getRequests().clear();
    }

    public static void renderMapAndRequests() {
        renderCityMap();
        renderPlanningRequest();
    }

    public static void renderCityMap() {
        //Add all the intersections temporarily
        for (Map.Entry<Long, Intersection> entry : cityMap.getIntersections().entrySet()) {
            Intersection intersection = entry.getValue();
            MapPoint mapPoint = new MapPoint(intersection.getCoordinates().getLatitude(), intersection.getCoordinates().getLongitude());
            pointLayer.addPoint(mapPoint, new Circle(2, Color.BLUE));
        }
    }

    private static int count = 1; //TODO Delete this ugly counter
    public static void renderPlanningRequest() {
        //Render the planning request
        Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
        MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
        ArrayList<RequestItem> items = new ArrayList<>();
        planningRequest.getRequests().forEach(request -> {
            //Items in list
            Pickup pickup = request.getPickup();
            RequestItem pickupItem = new RequestItem("Pickup at " + request.getPickup().getIntersection().getId(), "Duration: " + request.getPickup().getDuration(), count++);
            Delivery delivery = request.getDelivery();
            RequestItem deliveryItem = new RequestItem("Delivery at " + request.getDelivery().getIntersection().getId(), "Duration: " + request.getDelivery().getDuration(), count++);
            items.add(pickupItem);
            items.add(deliveryItem);
            //Map points
            pointLayer.addPoint(
                    new MapPoint(
                            pickup.getIntersection().getCoordinates().getLatitude(),
                            pickup.getIntersection().getCoordinates().getLongitude()
                    ),
                    new Circle(7, Color.RED)
            );
            pointLayer.addPoint(
                    new MapPoint(
                            delivery.getIntersection().getCoordinates().getLatitude(),
                            delivery.getIntersection().getCoordinates().getLongitude()
                    ),
                    new Circle(7, Color.GREEN)
            );
        });
        RequestView.setPickupItems(items);
        pointLayer.addPoint(depotPoint, new Circle(7, Color.ORANGE));
        //pointLayer.addPoint(depotPoint, new ImageView("/img/depotPin/depot.png")); //TODO Scale it with zoom level
    }

    public static PlanningRequest getPlanningRequest() {
        return planningRequest;
    }

    public static CityMap getCityMap() {
        return cityMap;
    }

}