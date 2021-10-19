package fr.insalyon.pldagile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PickyController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}