package fr.insalyon.pldagile.picky;

import fr.insalyon.pldagile.PickyApplication;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.view.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class Picky extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        CityMap citymap = new CityMap();
        PlanningRequest planningRequest = new PlanningRequest();
        Controller controller = new Controller(citymap, planningRequest);
        Window window = new Window(citymap, planningRequest, controller);
        window.start(stage);

        stage.show();
    }

    /**
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch();
    }

}
