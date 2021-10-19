package fr.insalyon.pldagile.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectionTest {

    @Test
    public void intersectionConstructorAndProperties() {
        Intersection intersect = new Intersection(1L, new Coordinates(11.25, 65.87));
        assertEquals(1, intersect.getId());
        assertEquals(11.25, intersect.getCoordinates().getLatitude());
        assertEquals(65.87, intersect.getCoordinates().getLongitude());
    }

}
