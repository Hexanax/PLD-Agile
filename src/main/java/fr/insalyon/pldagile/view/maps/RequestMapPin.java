package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.model.RequestType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

/**
 * Extends {@link MapPin}
 * Map pin to show the location of a request entity, either {@link fr.insalyon.pldagile.model.Pickup} or {@link fr.insalyon.pldagile.model.Delivery}
 */
public class RequestMapPin extends MapPin {

    private static final String PICKUP_ICON_URL = "img/pickupPin/pickup@2x.png";
    private static final String DELIVERY_ICON_URL = "img/deliveryPin/delivery@2x.png";
    private static final double TEXT_X_TRANSLATION_FACTOR = 0.22;
    private static final double TEXT_Y_TRANSLATION_FACTOR = -0.25;

    private final RequestType type;
    private final Long requestId;

    public RequestMapPin(RequestType type, Long requestId) {
        this.type = type;
        this.requestId = requestId;
        xOffsetFactor = -0.1;
        double iconSize = DEFAULT_ICON_SIZE;
        String imgURL, styleClass;
        if (type.equals(RequestType.PICKUP)) {
            imgURL = PICKUP_ICON_URL;
            styleClass = "pickup-pin";
        } else { // type.equals(RequestType.DELIVERY) == true
            imgURL = DELIVERY_ICON_URL;
            styleClass = "delivery-pin";
        }
        pin.setImage(new Image(
                imgURL,
                iconSize, iconSize,
                true, true
        ));

        // Add style properties to the pin
        pin.getStyleClass().add(styleClass);

        Label requestIdLabel = getRequestIdLabel();
        getChildren().addAll(pin, requestIdLabel);
        centerCoordinates();
    }

    private Label getRequestIdLabel() {
        String styleClass = type.equals(RequestType.PICKUP) ? "pickup-request-id" : "delivery-request-id";
        Label label = new Label(this.requestId.toString());
        label.setTranslateX(iconSize * TEXT_X_TRANSLATION_FACTOR);
        label.setTranslateY(iconSize * TEXT_Y_TRANSLATION_FACTOR);
        label.getStyleClass().add(styleClass);
        return label;
    }

    public RequestType getType() {
        return type;
    }
}