package fr.insalyon.pldagile.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconProvider {

    static public double ICON_SIZE = 35;

    public static Node getIcon(String iconName, double iconSize) {
        return new ImageView(
                new Image(
                        "img/buttonIcons/" + iconName + ".png",
                        iconSize, iconSize,
                        true, true
                )
        );
    }

    public static ImageView getDirectionIcon(double iconSize) {
        ImageView img = new ImageView(
                new Image("img/directionIcon/directionIcon.png", iconSize, iconSize, true, true));
        img.setY(-iconSize / 2);
        img.setX(-iconSize / 2);
        return img;
    }

}
