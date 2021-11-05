package fr.insalyon.pldagile.picky;

import fr.insalyon.pldagile.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Picky extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller();
        stage.show();
    }

    /**
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch();
    }

}
