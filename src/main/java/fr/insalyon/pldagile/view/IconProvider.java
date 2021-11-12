package fr.insalyon.pldagile.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class implements the icon service that provides the button and the directions icons
 */
public class IconProvider {

    static public double ICON_SIZE = 35;

    /**
     * Creates a new {@link ImageView} containing the wanted image based on its name and size
     * @param iconName the wanted icon's name
     * @param iconSize the wanted icon's size
     * @return a {@link ImageView}
     */
    public static Node getIcon(String iconName, double iconSize) {
        return new ImageView(
                new Image(
                        "img/buttonIcons/" + iconName + ".png",
                        iconSize, iconSize,
                        true, true
                )
        );
    }

    /**
     * Creates a new {@link ImageView} containing the direction's image based on its size
     * @param iconSize the wanted icon's size
     * @return a {@link ImageView}
     */
    public static ImageView getDirectionIcon(double iconSize) {
        ImageView img = new ImageView(
                new Image("img/directionIcon/directionIcon.png", iconSize, iconSize, true, true));
        img.setY(-iconSize / 2);
        img.setX(-iconSize / 2);
        return img;
    }

}
