package fr.insalyon.pldagile;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.ui.maps.MapPoint;
import fr.insalyon.pldagile.ui.maps.MapView;
import fr.insalyon.pldagile.ui.maps.PointLayer;
import fr.insalyon.pldagile.xml.ExceptionXML;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;
import fr.insalyon.pldagile.ui.maps.SidePanel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PickyApplication extends Application {

    private final static XMLDeserializer xmlDeserializer = new XMLDeserializer();
    private static Stage mainStage = null;

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void start(Stage stage) throws ExceptionXML {
        mainStage = stage;
        stage.setTitle("Picky - INSA Lyon");
        //Image desktopIcon = new Image(getClass().getResource("desktop-icon.png").toString());
        //stage.getIcons().add(desktopIcon);
        cityMap = new CityMap();
        planningRequest = new PlanningRequest();
        mapView = new MapView();
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
        bp.getChildren().addAll(mapView, headerLabel, copyright);
        headerLabel.setManaged(false);
        headerLabel.setVisible(false);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        mapView.setZoom(10);
        mapView.flyTo(0, new MapPoint(46.227638, 4.8357), 1.);
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

    public static void updateCityMap() {

    }

    public static void updatePlanningRequest() {
        Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
        MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
        PointLayer pointLayer = new PointLayer();
        pointLayer.addPoint(depotPoint, new Circle(7, Color.RED));
        mapView.addLayer(pointLayer);
    }

    public static PlanningRequest getPlanningRequest() {
        return planningRequest;
    }

    public static CityMap getCityMap() {
        return cityMap;
    }

}