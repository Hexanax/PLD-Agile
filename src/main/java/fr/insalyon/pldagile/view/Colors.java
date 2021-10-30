package fr.insalyon.pldagile.view;

import javafx.scene.paint.Color;

public abstract class Colors {
    static private String MAIN_BLUE = "0x3C8AFF";
    static private String LIGHT_GREY = "9A9A9A";


    static public Color getTourLineColor() {
        return Color.web(MAIN_BLUE);
    }

    static public Color getTourIntersectionColor() {
        return Color.web(MAIN_BLUE);
    }

    static public Color getMapIntersectionColor() {
        return Color.web(LIGHT_GREY);
    }
}
