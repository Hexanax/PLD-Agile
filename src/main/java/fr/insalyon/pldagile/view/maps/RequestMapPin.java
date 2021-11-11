package fr.insalyon.pldagile.view.maps;


import fr.insalyon.pldagile.model.RequestType;
import fr.insalyon.pldagile.view.Colors;
import fr.insalyon.pldagile.view.Fonts;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class RequestMapPin extends MapPin {
    private RequestType type;
    private Long requestId = 0L;

    private final String PICKUP_ICON_URL = "img/pickupPin/pickup@2x.png";
    private final String DELIVERY_ICON_URL = "img/deliveryPin/delivery@2x.png";

    private static final double TEXT_X_TRANSLATION_FACTOR = 0.22;
    private static final double TEXT_Y_TRANSLATION_FACTOR = -0.25;

    public RequestMapPin(RequestType type, Long requestId) {
        xOffsetFactor = -0.1;
        this.requestId = requestId;
        double iconSize = DEFAULT_ICON_SIZE;
        this.type = type;
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

        Text requestIdLabel = getRequestIdLabel();
        getChildren().addAll(pin, requestIdLabel);
        centerCoordinates();
    }

    private Text getRequestIdLabel() {
        Text label = new Text(this.requestId.toString());
        label.setTranslateX(iconSize * TEXT_X_TRANSLATION_FACTOR);
        label.setTranslateY(iconSize * TEXT_Y_TRANSLATION_FACTOR);
        label.setFill(Colors.getRequestIdLabelColor(this.type));
        label.setFont(Fonts.getBodyFont());
        return label;
    }

    public Long getRequestId() {
        return requestId;
    }

    public RequestType getType() {
        return type;
    }
}