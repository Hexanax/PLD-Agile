package fr.insalyon.pldagile.view.maps;

import javafx.scene.image.Image;

public class DepotMapPin extends MapPin {
    private static final String IMG_URL = "img/depotPin/depot@2x.png";

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