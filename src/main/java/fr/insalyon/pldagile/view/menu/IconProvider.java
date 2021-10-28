package fr.insalyon.pldagile.view.menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class IconProvider {
    public static ImageView getIcon(String iconName, double iconSize ) {
        return new ImageView(
                new Image(
                        "img/buttonIcons/" + iconName + ".png", iconSize, iconSize, false, true
                )
        );
    }
}
