package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.view.maps.MapLayer;
import fr.insalyon.pldagile.view.maps.MapPoint;
import fr.insalyon.pldagile.view.maps.PointLayer;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class CityMapView implements PropertyChangeListener {

    private final PointLayer cityPointLayer = new PointLayer();
    private Controller controller;
    private CityMap cityMap;

    public CityMapView(Controller controller) {
        this.controller = controller;
        this.cityMap = controller.getPclCityMap().getCityMap();
        controller.getPclCityMap().addPropertyChangeListener(this);
        render();
    }

    //TODO Add View interface with render method
    public void render() {
        if(cityMap != null) {
            System.out.println("Entered");
            cityPointLayer.clearPoints();
            for (Map.Entry<Long, Intersection> entry : cityMap.getIntersections().entrySet()) {
                System.out.println("loop");
                Intersection intersection = entry.getValue();
                MapPoint mapPoint = new MapPoint(intersection.getCoordinates().getLatitude(), intersection.getCoordinates().getLongitude());
                mapPoint.setId(intersection.getId());
                cityPointLayer.addPoint(mapPoint, new Circle(2, Colors.getMapIntersectionColor()));
            }
        }
    }

    public MapLayer getLayer() {
        return cityPointLayer;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("CityMapView event " + evt);
        this.cityMap = (CityMap) evt.getNewValue();
        render();
    }

//    public void activeMapIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : cityPointLayer.get) {
//            point.getValue().setOnMouseClicked(event-> {
//                controller.modifyClick(point.getKey().getId(),"Intersection", -1);
//            });
//        }
//    }
//
//    public void disableMapIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : intersectionPoints) {
//            point.getValue().setOnMouseClicked(null);
//        }
//    }

}