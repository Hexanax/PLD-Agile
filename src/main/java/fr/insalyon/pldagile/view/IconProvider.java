package fr.insalyon.pldagile.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class IconProvider {

    static public double ICON_SIZE = 35;

    public static ImageView getIcon(String iconName, double iconSize) {
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
    public static ImageView getDoubleDirectionIcon(double iconSize) {
        ImageView img = new ImageView(
                new Image("img/doubleDirectionIcon/doubleDirectionIcon.png", iconSize, iconSize, true, true));
        img.setY(-iconSize / 2);
        img.setX(-iconSize / 2);
        System.out.println("double sens picked");
        return img;
    }
}
