package fr.insalyon.pldagile.view;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Fonts {
    public static Font getBodyFont() {
        return Font.font("Roboto", FontWeight.NORMAL, 10);
    }

    public static Font getBoldBodyFont() {
        return Font.font("Roboto", FontWeight.BOLD, 12);
    }
}
