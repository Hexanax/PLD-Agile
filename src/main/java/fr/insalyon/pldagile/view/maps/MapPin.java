package fr.insalyon.pldagile.view.maps;


import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * This class represents a pin on the map, it represents a visual indication styled as a ping.
 * It is very useful to highlight locations of interest
 */
public class MapPin extends StackPane {
    protected static double DEFAULT_ICON_SIZE = 35D;

    protected double iconSize = DEFAULT_ICON_SIZE;
    protected ImageView pin = new ImageView();
    protected double yOffsetFactor = 0.5;
    protected double xOffsetFactor = 0.05;

    /**
     * Adjusts the coordinates of the view so that their origin is at the bottom center of
     * the pin and not at its top left corner
     */
    protected void centerCoordinates() {
        setCenteredX();
        setCenteredY();
    }

    /**
     * Sets the new Y origin on the icon
     */
    private void setCenteredY() {
        this.setLayoutY(getLayoutBounds().getMinY() - iconSize * yOffsetFactor);
    }

    /**
     * Sets the new X origin on the icon
     */
    private void setCenteredX() {
        this.setLayoutX(getLayoutBounds().getMinX() - iconSize * xOffsetFactor);
    }

}