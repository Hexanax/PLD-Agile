module fr.insalyon.pldagile {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens fr.insalyon.pldagile to javafx.fxml;
    exports fr.insalyon.pldagile;
}