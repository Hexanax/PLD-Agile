package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.model.RequestType;
import javafx.scene.image.Image;

public class RequestMapPin extends MapPin {
    private RequestType type;
    private Long requestId;
    private final String PICKUP_ICON_URL = "img/pickupPin/pickup@2x.png";
    private final String DELIVERY_ICON_URL = "img/deliveryPin/delivery@2x.png";

    public RequestMapPin(RequestType type){
        double iconSize = DEFAULT_ICON_SIZE;
        this.type = type;
        String imgURL = type.equals(RequestType.PICKUP) ? PICKUP_ICON_URL : DELIVERY_ICON_URL;
        this.setImage(new Image(
                imgURL,
                iconSize, iconSize,
                true, true
        ));
        this.centerCoordinates();
    }
}
