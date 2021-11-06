package fr.insalyon.pldagile.view;

import javafx.scene.paint.Color;

public class Colors {
    private static final String MAIN_BLUE = "#3C8AFF";
    private static final String LIGHT_GREY = "#9A9A9A";
    private static final String RED = "#FF0000";

    public static Color getTourLineColor() {
        return Color.web(MAIN_BLUE);
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

}