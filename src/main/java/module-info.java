module fr.insalyon.pldagile {
    uses fr.insalyon.pldagile.ui.maps.tile.TileRetriever;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens fr.insalyon.pldagile to javafx.fxml;
    exports fr.insalyon.pldagile;
}