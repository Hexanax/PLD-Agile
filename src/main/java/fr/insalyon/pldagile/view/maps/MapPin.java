package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.model.RequestType;
import javafx.scene.image.ImageView;

public abstract class MapPin extends ImageView {
    static protected double DEFAULT_ICON_SIZE = 35D;
    protected double iconSize = DEFAULT_ICON_SIZE;

    /**
     * Adjusts the coordinates of the view so that their origin is at the bottom center of
     * the pin and not at its top left corner
     */
    protected void centerCoordinates(){
        setCenteredX();
        setCenteredY();
    }

    /**
     * Sets the new Y origin on the icon
     */
    private void setCenteredY(){
        setY(-iconSize);
    }

    /**
     * Sets the new X origin on the icon
     */
    private void setCenteredX(){
        super.setX(-iconSize / 2.6); // Somehow, dividing by 2 makes it not on the intersection
                                    // dividing by 2.6 looks closer to the real intersection
    }

}
