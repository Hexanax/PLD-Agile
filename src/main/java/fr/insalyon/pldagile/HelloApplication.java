package fr.insalyon.pldagile;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.ui.maps.MapPoint;
import fr.insalyon.pldagile.ui.maps.MapView;
import fr.insalyon.pldagile.xml.ExceptionXML;
import fr.insalyon.pldagile.xml.XMLDeserializer;
import fr.insalyon.pldagile.xml.XMLFileOpener;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {

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
        MapView view = new MapView();
        view.setZoom(3);
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
        Scene scene = new Scene(bp, 1200, 700);
        bp.getChildren().addAll(view, headerLabel, copyright);
        headerLabel.setManaged(false);
        headerLabel.setVisible(false);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        view.setZoom(10);
        view.flyTo(0, new MapPoint(46.227638, 4.8357), 1.);

        try {
            CityMap citymap = new CityMap();
            xmlDeserializer.load(citymap);
        } catch(Exception e) {
            e.printStackTrace();
        }

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
}