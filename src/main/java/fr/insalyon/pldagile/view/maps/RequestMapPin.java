package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.model.RequestType;
import javafx.scene.image.Image;

public class RequestMapPin extends MapPin {
    private RequestType type;
    private Long requestId;
    private String PICKUP_ICON_URL = "img/pickupPin/pickup@2x.png";
    private String DELIVERY_ICON_URL = "img/deliveryPin/delivery@2x.png";

    public RequestMapPin getPickupPin(){
        return RequestMapPin(RequestType.PICKUP, PICKUP_ICON_URL);
    }
    private RequestMapPin(RequestType type, String imgURL){
        double iconSize = super.DEFAULT_ICON_SIZE;
        type = type;
        this.setImage(new Image(
                imgURL,
                iconSize, iconSize,
                true, true
        ));
        this.centerCoordinates();
    }
}
