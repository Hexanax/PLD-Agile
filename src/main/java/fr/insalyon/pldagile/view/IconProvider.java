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

    public static ImageView getPickupIcon() {
        ImageView img = new ImageView(
                new Image(
                        "img/pickupPin/pickup@2x.png",
                        ICON_SIZE, ICON_SIZE,
                        true, true
                )
        );

        // Adjust coordinates of teh view so that their origin is at the bottom center of the pin
        img.setY(-ICON_SIZE);
        img.setX(-ICON_SIZE / 2);

        return img;
    }

    public static ImageView getDropoffIcon() {
        ImageView img = new ImageView(
                new Image(
                        "img/deliveryPin/delivery@2x.png",
                        ICON_SIZE, ICON_SIZE,
                        true, true
                ));

        // Adjust coordinates of teh view so that their origin is at the bottom center of the pin
        img.setY(-ICON_SIZE);
        img.setX(-ICON_SIZE / 2);

        return img;
    }

    public static ImageView getDirectionIcon(double iconSize) {
        ImageView img = new ImageView(
                new Image("img/directionIcon/directionIcon.png", iconSize, iconSize, true, true));
        img.setY(-iconSize / 2);
        img.setX(-iconSize / 2);
        return img;
    }

}
