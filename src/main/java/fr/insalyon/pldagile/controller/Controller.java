package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.view.Window;

public class Controller {
    private CityMap citymap;
    private State currentState;
    private Window window;

    public Controller(CityMap citymap) {
        this.citymap = citymap;
        this.window = new Window();
    }
}
