package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.maps.*;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TourView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<ImageView> tourDirectionLayer = new PointLayer<>();
    private final PointLayer<Circle> tourPointLayer = new PointLayer<>();
    private final LineLayer tourLineLayer = new LineLayer();
    private Controller controller;
    private Tour tour;

    public TourView(Controller controller) {
        this.controller = controller;
        this.tour = controller.getPclTour().getTour();
        controller.getPclTour().addPropertyChangeListener(this);
    }

    /**
     * Clear the points and lines of the map
     */
    public void clear() {
        tourLineLayer.clearLines();
        tourPointLayer.clearPoints();
        tourDirectionLayer.clearPoints();
    }

    public PointLayer<Circle> getTourPointLayer() {
        return tourPointLayer;
    }

    public LineLayer getTourLineLayer() {
        return tourLineLayer;
    }

    public PointLayer<ImageView> getTourDirectionLayer() {
        return tourDirectionLayer;
    }

    /**
     * Rendering the points and segments between them
     */
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

            DropShadow borderEffect = new DropShadow(BlurType.THREE_PASS_BOX, Colors.getTourLineStrokeColor(), 2, 1, 0, 0);
            tourLineLayer.addLine(mapDestination, Colors.getTourLineColor(), borderEffect, 4);

            DropShadow tourPointBorderEffect = new DropShadow(BlurType.THREE_PASS_BOX, Colors.getTourLineColor(), 2, 3, 0, 0);
            tourPointLayer.addPoint(originPoint, new Circle(2, Colors.getTourLinePointColor()), tourPointBorderEffect);
            tourPointLayer.addPoint(destinationPoint, new Circle(2, Colors.getTourLinePointColor()), tourPointBorderEffect);

            MapPoint midPoint = new MapPoint(originPoint.getLatitude() + (destinationPoint.getLatitude() - originPoint.getLatitude()) / 2,
                    originPoint.getLongitude() + (destinationPoint.getLongitude() - originPoint.getLongitude()) / 2);

            //Line layout effect as a callback to compute the angle to rotate the direction point
            tourLineLayer.addLineLayoutEffect(new LineLayoutEffect() {
                @Override
                public void layout(MapDestination destination, Line line, Point2D originPoint, Point2D destinationPoint) {
                    if (mapDestination == destination) {
                        ImageView directionArrowImage = IconProvider.getDirectionIcon(20);
                        double lineLength = originPoint.distance(destinationPoint);
                        if (lineLength >= directionArrowImage.getImage().getWidth()) {
                            if (!tourDirectionLayer.containsPoint(midPoint)) {
                                tourDirectionLayer.addPoint(midPoint, directionArrowImage);
                            }
                            //Rotate the image of the arrow, so it points in the direction of the end point
                            double rotateAngle = originPoint.angle(new Point2D(originPoint.getX() + 1, originPoint.getY()),
                                    destinationPoint);
                            /*
                             * Point2D.angle() will compute the smallest angle possible between the angle in
                             * the counter clockwise and the clockwise directions. However, the
                             * ImageView.rotate function rotates counter clockwise only. If the start point
                             * (startProjected) is below (has a lower y value) than the end point
                             * (endProjected), Point2D.angle(), will use the angle for the clockwise
                             * direction, thus, we must convert it to an angle in the counter clockwise
                             * direction.
                             */
                            if (destinationPoint.getY() < originPoint.getY()) {
                                rotateAngle = -rotateAngle;
                            }
                            directionArrowImage.setRotate(rotateAngle);
                        } else {
                            tourDirectionLayer.removePoint(midPoint);
                        }
                    }
                }
            });

//            direction.setTranslateX(midProjected.getX());
//            direction.setTranslateY(midProjected.getY());
//
//            // Rotate the image of the arrow so it points in the direction of the end point
//            double rotateAngle = startProjected.angle(new Point2D(startProjected.getX() + 1, startProjected.getY()),
//                    endProjected);

            //Update prev intersection
            previousIntersection = destinationIntersection;
        }

    }

    /**
     * Hide the points and segments
     */
    @Override
    public void hide() {
        tourPointLayer.hide();
        tourLineLayer.hide();
        tourDirectionLayer.hide();
    }

    /**
     * Show the points and segments
     */
    @Override
    public void show() {
        tourLineLayer.show();
        tourPointLayer.show();
        tourDirectionLayer.show();
    }

    /**
     * Update the event passed in parameter
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ////System.out.println("TourViewEvent event " + evt);
        this.tour = (Tour) evt.getNewValue();
        render();
    }
}