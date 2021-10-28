package fr.insalyon.pldagile.view.maps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.awt.*;

/**
 * A layer that allows to draw lines between two map points.
 */
public class LineLayer extends MapLayer {

    // TODO Pair<MapPoint, MapPoint> define MapLine
    private final ObservableList<Pair<MapDestination, Line>> lines = FXCollections.observableArrayList();

    public LineLayer() {

    }

    public void addLine(MapDestination mapDestination, Color color) {
        Line line = new Line();
        line.setStroke(color);
        line.setStrokeWidth(4);
        lines.add(new Pair<>(mapDestination, line));
        this.getChildren().add(line); // TODO Reflect where to add the line node child the cleanest way
        this.markDirty();
    }

    public void clearPoints() {
        lines.clear();
        this.getChildren().clear();
        this.markDirty();
    }

    @Override
    protected void layoutLayer() {
        for (Pair<MapDestination, Line> candidate : lines) {
            MapDestination destination = candidate.getKey();
            Line line = candidate.getValue();
            MapPoint start = destination.getStart();
            MapPoint end = destination.getEnd();

            Point2D startProjected = getMapPoint(start.getLatitude(), start.getLongitude());
            Point2D endProjected = getMapPoint(end.getLatitude(), end.getLongitude());
            // Line line = new Line(startProjected.getX(), startProjected.getY(),
            // endProjected.getX(), endProjected.getY());
            // icon.setTranslateX(mapPoint.getX());
            // icon.setTranslateY(mapPoint.getY());
            line.setStartX(startProjected.getX());
            line.setStartY(startProjected.getY());
            line.setEndX(endProjected.getX());
            line.setEndY(endProjected.getY());
            line.setVisible(true);
        }
    }

}
