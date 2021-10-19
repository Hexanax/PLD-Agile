package fr.insalyon.pldagile.model;

import org.junit.jupiter.api.Test;

import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Coordinates;

public class IntersectionTest {
    @Test
    public void creation1() {
        Intersection intersect = new Intersection((long) 1, new Coordinates(11.25,65.87));
        assert( (int) intersect.getId() ==1);
        assert( intersect.getCoordinates().getLatitude()==11.25);
        assert( intersect.getCoordinates().getLongitude()==65.87);
    }

}
