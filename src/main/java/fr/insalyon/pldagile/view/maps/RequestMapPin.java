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
        String imgURL, styleClass;
        if (type.equals(RequestType.PICKUP)){
            imgURL = PICKUP_ICON_URL;
            styleClass = "pickup-pin";

        } else { // type.equals(RequestType.DELIVERY) == true
            imgURL = DELIVERY_ICON_URL;
            styleClass = "delivery-pin";
        }
        this.setImage(new Image(
                imgURL,
                iconSize, iconSize,
                true, true
        ));
        super.centerCoordinates();
        // Add style properties to the pin
        this.getStyleClass().add(styleClass);
    }

    public Long getRequestId() {
        return requestId;
    }

}
