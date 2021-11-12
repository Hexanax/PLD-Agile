package fr.insalyon.pldagile.view.maps;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 * This interface defines layout effects to be added in [LineLayer]
 * to add extra rendering effects and/or data hooking from each line layer rendering.
 */
public interface LineLayoutEffect {
    void layout(MapDestination mapDestination, Line line, Point2D originPoint, Point2D destinationPoint);
}