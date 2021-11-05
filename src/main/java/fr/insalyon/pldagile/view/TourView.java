package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.PointLayer;

public class TourView {

    private final PointLayer tourPointLayer = new PointLayer();
    private Controller controller;

    public TourView(Controller controller) {
        this.controller = controller;
    }

}