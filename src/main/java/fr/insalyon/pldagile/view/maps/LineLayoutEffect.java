package fr.insalyon.pldagile.view.maps;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public interface LineLayoutEffect {
    void layout(MapDestination mapDestination, Line line, Point2D originPoint, Point2D destinationPoint);
}