package fr.insalyon.pldagile.view.maps;

import javafx.scene.image.Image;

import java.util.function.Supplier;

public class LoadingImageSupplier implements Supplier<Image> {

    @Override
    public Image get() {
        return new Image("/img/tile-placeholder.png");
    }

}
