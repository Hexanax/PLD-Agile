package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.model.RequestType;
import javafx.scene.paint.Color;

public class Colors {
    private static final String MAIN_BLUE = "3C8AFF";
    private static final String DARK_MAIN_BLUE = "3373D3";
    private static final String DARKER_MAIN_BLUE = "002153";

    private static final String LIGHT_GREY = "9A9A9A";
    private static final String RED = "FF0000";

    private static final String DARK_MAIN_ORANGE = "532800";

    public static Color getTourLineColor() {
        return Color.web(MAIN_BLUE);
    }

    public static Color getRequestIdLabelColor(RequestType type) {
        if (type.equals(RequestType.PICKUP)) {
            return Color.web(DARKER_MAIN_BLUE);
        } else {
            return Color.web(DARK_MAIN_ORANGE);
        }
    }

    public static Color getTourIntersectionColor() {
        return Color.web(MAIN_BLUE);
    }

    public static Color getMapIntersectionColor() {
        return Color.web(LIGHT_GREY);
    }

    public static Color getMapIntersectionSelectColor() {
        return Color.web(RED);
    }

    public static Color getTourLineStrokeColor() {
        return Color.web(DARK_MAIN_BLUE);
    }

    public static Color getTourLinePointColor() {
        return Color.WHITE;
    }

}