package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.maps.*;
import javafx.scene.shape.Circle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TourView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<Circle> tourPointLayer = new PointLayer<>();
    private final LineLayer tourLineLayer = new LineLayer();
    private Controller controller;
    private Tour tour;

    public TourView(Controller controller) {
        this.controller = controller;
        this.tour = controller.getPclTour().getTour();
        controller.getPclTour().addPropertyChangeListener(this);
    }

    public void clear() {
        tourLineLayer.clearPoints();
        tourPointLayer.clearPoints();
    }

    //TODO Check usage => Potential side effects ?
    public MapLayer getTourPointLayer() {
        return tourPointLayer;
    }

    //TODO Check usage => Potential side effects ?
    public LineLayer getTourLineLayer() {
        return tourLineLayer;
    }

    @Override
    public void render() {
        clear();
        Tour tour = this.tour;
        // TODO Update RequestView
        //if the tour is a null object, we just clear
        if (tour.getDepot() == null) {
            return;
        }
        Intersection previousIntersection = tour.getDepot().getIntersection();
        for (Segment segment : tour.getPath()) {
            Intersection destinationIntersection = segment.getDestination();
            MapPoint originPoint = new MapPoint(previousIntersection.getCoordinates().getLatitude(), previousIntersection.getCoordinates().getLongitude());
            MapPoint destinationPoint = new MapPoint(destinationIntersection.getCoordinates().getLatitude(), destinationIntersection.getCoordinates().getLongitude());
            MapDestination mapDestination = new MapDestination(originPoint, destinationPoint);
            tourPointLayer.addPoint(originPoint, new Circle(4, Colors.getTourIntersectionColor()));
            tourPointLayer.addPoint(destinationPoint, new Circle(4, Colors.getTourIntersectionColor()));
            tourLineLayer.addLine(mapDestination, Colors.getTourIntersectionColor());
            //Update prev intersection
            previousIntersection = destinationIntersection;
        }
    }

    @Override
    public void hide() {
        tourPointLayer.hide();
    }

    @Override
    public void show() {
        tourLineLayer.show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("TourViewEvent event " + evt);
        this.tour = (Tour) evt.getNewValue();
        render();
    }
}