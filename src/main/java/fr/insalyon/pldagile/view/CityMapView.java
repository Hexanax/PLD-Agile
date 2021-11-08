package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.view.maps.MapLayer;
import fr.insalyon.pldagile.view.maps.MapPoint;
import fr.insalyon.pldagile.view.maps.PointLayer;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class CityMapView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<Circle> cityPointLayer = new PointLayer<>();
    private final Controller controller;
    private CityMap cityMap;

    public CityMapView(Controller controller) {
        this.controller = controller;
        this.cityMap = controller.getPclCityMap().getCityMap();
        controller.getPclCityMap().addPropertyChangeListener(this);
        render();
    }

    public MapLayer getLayer() {
        return cityPointLayer;
    }

    public void setIntersectionColor(Color color) {
        cityPointLayer.getPoints().forEach(point -> {
            Circle circle = point.getValue();
            circle.setFill(color);
        });
    }



    @Override
    public void hide() {
        cityPointLayer.hide();
    }

    @Override
    public void show() {
        cityPointLayer.show();
    }


    public void highlight(){
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setRadius(4);
            point.getValue().setFill(Colors.getMapIntersectionSelectColor());
        }
    }

    public void unHighlight(){
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setRadius(2);
            point.getValue().setFill(Colors.getMapIntersectionColor());
        }
    }

    @Override
    public void render() {
        if (cityMap != null) {
            System.out.println("Entered");
            clear();
            for (Map.Entry<Long, Intersection> entry : cityMap.getIntersections().entrySet()) {
                Intersection intersection = entry.getValue();
                MapPoint mapPoint = new MapPoint(intersection.getCoordinates().getLatitude(), intersection.getCoordinates().getLongitude());
                mapPoint.setId(intersection.getId());
                cityPointLayer.addPoint(mapPoint, new Circle(2, Colors.getMapIntersectionColor()));
            }
            activeMapIntersectionsListener();

        }
    }

    private void clear() {
        cityPointLayer.clearPoints();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("CityMapView event " + evt);
        this.cityMap = (CityMap) evt.getNewValue();
        render();
    }

    /**
     * Makes map intersections clickable
     */
    public void activeMapIntersectionsListener() {
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setOnMouseClicked(event-> {
                System.out.println(point.getKey().getId());
                controller.intersectionClick(point.getKey().getId());
            });
        }
    }

}