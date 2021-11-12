package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.view.maps.MapLayer;
import fr.insalyon.pldagile.view.maps.MapPoint;
import fr.insalyon.pldagile.view.maps.PointLayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Pair;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * This class represents the city map view along with its intersections' icons
 * it shows, hides, highlights and unhighlights these icons
 */
public class CityMapView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<Circle> cityPointLayer = new PointLayer<>();
    private final Controller controller;
    private CityMap cityMap;


    /**
     * Creates a cityMapView component
     * @param controller
     */
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


    /**
     * hides the map's intersections icons by hiding the {@link PointLayer} cityPointLayer
     */
    @Override
    public void hide() {
        cityPointLayer.hide();
    }

    /**
     * show the map's intersections icons by showing the {@link PointLayer} cityPointLayer
     */
    @Override
    public void show() {
        cityPointLayer.show();
    }

    /**
     * highlights the intersection's icons by making their shapes bigger
     * and take a different color
     */
    public void highlight(){
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setRadius(2);
            point.getValue().setFill(Colors.getMapIntersectionSelectColor());
            point.getValue().setStroke(Colors.getMapIntersectionSelectStrokeColor());
            point.getValue().setStrokeWidth(3);
            point.getValue().setStrokeType(StrokeType.OUTSIDE);
        }
    }

    /**
     * unhighlights the intersection's icons
     */
    public void unHighlight(){
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setRadius(2);
            point.getValue().setFill(Colors.getMapIntersectionColor());
            point.getValue().setStrokeWidth(0);
        }
    }

    /**
     * loops through the map entry and adds the intersection's icons
     * then activates the listener
     */
    @Override
    public void render() {
        if (cityMap != null) {
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


    /**
     * Clears the map's intersections icons
     */
    private void clear() {
        cityPointLayer.clearPoints();
    }

    /**
     * Gets the event that makes the property change and render
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.cityMap = (CityMap) evt.getNewValue();
        render();
    }

    /**
     * Makes map intersections clickable
     */
    public void activeMapIntersectionsListener() {
        for (Pair<MapPoint, Circle> point : cityPointLayer.getPoints()) {
            point.getValue().setOnMouseClicked(event-> controller.intersectionClick(point.getKey().getId()));
        }
    }

}