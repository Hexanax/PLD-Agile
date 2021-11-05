package fr.insalyon.pldagile.picky;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class PickyApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Controller controller = new Controller();
        controller.getWindow().render(stage);
        stage.show();
    }

    /**
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch();
    }

}
