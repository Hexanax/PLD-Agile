package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.view.Colors;
import fr.insalyon.pldagile.view.IconProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Pair;


/**
 * A layer that allows to draw lines between two map points.
 */
public class LineLayer extends MapLayer {

    private final ObservableList<DirectedLine> lines = FXCollections
            .observableArrayList();

    public LineLayer() {
        
    }

    public void addLine(MapDestination mapDestination) {

        MapDestination invertedDirectionMD = new MapDestination(mapDestination.getEnd(), mapDestination.getStart());
        DirectedLine  invertedDirectionDL = new DirectedLine(invertedDirectionMD);
        ImageView directionIcon = lines.contains(invertedDirectionDL) ?
                IconProvider.getDoubleDirectionIcon(20) :
                IconProvider.getDirectionIcon(20);

        DirectedLine directedLine = new DirectedLine(mapDestination);
        directedLine.setDirection(directionIcon);

        lines.add(directedLine);
        this.getChildren().add(directedLine.getLine());
        this.getChildren().add(directedLine.getDirection());
        this.markDirty();
    }

    public void clearPoints() {
        lines.clear();
        this.getChildren().clear();
        this.markDirty();
    }

    @Override
    protected void layoutLayer() {
        // Pair<MapDestination, Pair<Line, ImageView>>
        for (DirectedLine candidate : lines) {
            MapDestination destination = candidate.getMapDestination();
            Line line = candidate.getLine();
            ImageView direction = candidate.getDirection();
            MapPoint start = destination.getStart();
            MapPoint end = destination.getEnd();

            // Draw the line
            Point2D startProjected = getMapPoint(start.getLatitude(), start.getLongitude());
            Point2D endProjected = getMapPoint(end.getLatitude(), end.getLongitude());

            line.setStartX(startProjected.getX());
            line.setStartY(startProjected.getY());

            line.setEndX(endProjected.getX());
            line.setEndY(endProjected.getY());
            line.setVisible(true);

            // Draw the direction
            // The icon's position is exactly at the middle of the line
            Point2D midProjected = getMapPoint(start.getLatitude() + (end.getLatitude() - start.getLatitude()) / 2,
                    start.getLongitude() + (end.getLongitude() - start.getLongitude()) / 2);
            direction.setTranslateX(midProjected.getX());
            direction.setTranslateY(midProjected.getY());

            // Rotate the image of the arrow so it points in the direction of the end point
            double rotateAngle = startProjected.angle(new Point2D(startProjected.getX() + 1, startProjected.getY()),
                    endProjected);
            /*
             * Point2D.angle() will compute the smallest angle possible between the angle in
             * the counter clockwise and the clockwise directions. However, the
             * ImageView.rotate function rotates counter clockwise only. If the start point
             * (startProjected) is below (has a lower y value) than the end point
             * (endProjected), Point2D.angle(), will use the angle for the clockwise
             * direction, thus, we must convert it to an angle in the counter clockwise
             * direction.
             */
            if (endProjected.getY() < startProjected.getY()) {
                rotateAngle = -rotateAngle;
            }
            direction.setRotate(rotateAngle);

            direction.setVisible(true);
        }
    }

}
