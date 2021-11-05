package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.view.Colors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.awt.*;

/**
 * A layer that allows to draw lines between two map points.
 */
public class LineLayer extends MapLayer {

    private final ObservableList<Pair<MapDestination, Line>> lines = FXCollections.observableArrayList();

    public LineLayer() {
        
    }

    public void addLine(MapDestination mapDestination) {
        Line line = new Line();
        line.setStroke(Colors.getTourLineColor());
        line.setStrokeWidth(4);
        DropShadow borderEffect = new DropShadow(BlurType.THREE_PASS_BOX, Colors.getTourLineStrokeColor(), 2, 1, 0, 0);
        line.setEffect(borderEffect);
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
            line.setStartX(startProjected.getX());
            line.setStartY(startProjected.getY());
            line.setEndX(endProjected.getX());
            line.setEndY(endProjected.getY());
            line.setVisible(true);
        }
    }

}
