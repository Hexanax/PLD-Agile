package fr.insalyon.pldagile.view.maps;

import javafx.scene.image.Image;

/**
 * This class creates the icon (pin) for the depot using a png image
 */
public class DepotMapPin extends MapPin {
    private static final String IMG_URL = "img/depotPin/depot@2x.png";

    /**
     * creates the depot map pin
     */
    public DepotMapPin() {
        pin.setImage(new Image(
                IMG_URL,
                iconSize, iconSize,
                true, true
        ));
        this.getStyleClass().add("depot-pin");
        getChildren().addAll(pin);
        super.centerCoordinates();
    }
}