package fr.insalyon.pldagile.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatesTest {

    @Test
    public void coordinatesConstructorAndPropertiesTest() {
        Coordinates coords = new Coordinates(45.8,5.0);
        assertEquals(45.8,coords.getLatitude());
        assertEquals(5.0,coords.getLongitude());

    }

    @Test
    public void coordinatesSetterTest() {
        Coordinates coords = new Coordinates(45.8,5.0);
        coords.setLatitude(45.0);
        coords.setLongitude(5.5);
        assertEquals(45.0,coords.getLatitude());
        assertEquals(5.5,coords.getLongitude());

    }

}