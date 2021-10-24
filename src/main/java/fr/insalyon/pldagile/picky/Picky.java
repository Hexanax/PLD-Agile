package fr.insalyon.pldagile.picky;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.CityMap;

public class Picky {
    /**
     * @param args the arguments
     */
    public static void main(String[] args) {
        CityMap citymap = new CityMap();
        new Controller(citymap);
    }
}
