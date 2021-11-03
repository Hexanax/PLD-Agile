package fr.insalyon.pldagile.view.maps;

import javafx.scene.image.Image;

public class DepotMapPin extends MapPin {
    private final String IMG_URL = "img/depotPin/depot@2x.png";

    public DepotMapPin(){
        this.setImage(new Image(
                IMG_URL,
                iconSize, iconSize,
                true, true
        ));
        super.centerCoordinates();
        this.getStyleClass().add("depot-pin");
    }
}
