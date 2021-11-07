package fr.insalyon.pldagile.view;

import javafx.scene.paint.Color;

public class Colors {
    private static final String MAIN_BLUE = "#3C8AFF";
    private static final String LIGHT_GREY = "#9A9A9A";
    private static final String RED = "#FF0000";
    private static final String DARK_MAIN_BLUE = "3373D3";

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

    public static Color getTourLineStrokeColor() {
        return Color.web(DARK_MAIN_BLUE);
    }

    public static Color getTourLinePointColor() {
        return Color.WHITE;
    }

}