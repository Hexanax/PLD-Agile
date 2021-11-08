package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.view.Colors;
import fr.insalyon.pldagile.view.Hideable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;

/**
 * A layer that allows to draw lines between two map points.
 */
public class LineLayer extends MapLayer implements Hideable {

    private final ObservableList<Pair<MapDestination, Line>> lines = FXCollections.observableArrayList();
    private final ArrayList<LineLayoutEffect> layoutEffects = new ArrayList<>();
    private boolean shown = true;

    public LineLayer() { }

    public void addLineLayoutEffect(LineLayoutEffect lineLayoutEffect) {
        layoutEffects.add(lineLayoutEffect);
    }

    public void addLine(MapDestination mapDestination, Color color, Effect effect, double strokeWidth) {
        Line line = new Line();
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        line.setEffect(effect);
        lines.add(new Pair<>(mapDestination, line));
        if(shown) {
            this.getChildren().add(line);
            this.markDirty();
        }
    }

    public void clearPoints() {
        lines.clear();
        this.getChildren().clear();
        this.markDirty();
    }

    @Override
    protected void layoutLayer() {
        for (Pair<MapDestination, Line> candidate : lines) {
            //Render the line
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

            //Call the additional layout effects
            layoutEffects.forEach(layoutEffect -> layoutEffect.layout(destination, line, startProjected, endProjected));
        }
    }

    @Override
    public void hide() {
        this.getChildren().clear();
        this.markDirty();
        shown = false;
    }

    @Override
    public void show() {
        if(!shown) {
            lines.forEach(line -> this.getChildren().add(line.getValue()));
            this.markDirty();
            shown = true;
        }
    }

}