package fr.insalyon.pldagile.picky;

import fr.insalyon.pldagile.PickyApplication;
import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.PlanningRequest;
import fr.insalyon.pldagile.model.Tour;
import fr.insalyon.pldagile.view.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class Picky extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        CityMap citymap = new CityMap();
        PlanningRequest planningRequest = new PlanningRequest();
        Tour tour = new Tour();
        Controller controller = new Controller(citymap, planningRequest, tour);
        Window window = new Window(controller);
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
