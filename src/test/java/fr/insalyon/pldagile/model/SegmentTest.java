package fr.insalyon.pldagile.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SegmentTest {

    @Test
    public void segmentConstructorTest(){
        Intersection origin = new Intersection(6L, new Coordinates(45.8,4.8));
        Intersection destination = new Intersection(7L, new Coordinates(45.8,5));
        Segment segment = new Segment("Rue de l'église",15500,origin,destination);
        assertEquals(segment.getLength(),15500);
        assertEquals(segment.getOrigin(),origin);
        assertEquals(segment.getDestination(),destination);
        assertEquals(segment.getName(),"Rue de l'église");
    }

    @Test
    public void segmentSettersTest(){
        Intersection origin = new Intersection(6L, new Coordinates(45.8,4.8));
        Intersection destination = new Intersection(7L, new Coordinates(45.8,5));
        Segment segment = new Segment("Rue de l'église",15500,origin,destination);
        Intersection newDestination = new Intersection(1L, new Coordinates(45.5,4.8));
        Intersection newOrigin = new Intersection(4L, new Coordinates(45.5,5));
        segment.setDestination(newDestination);
        segment.setOrigin(newOrigin);
        segment.setLength(15590);
        segment.setName("Boulevard Belateche");
        assertEquals(segment.getLength(),15590);
        assertEquals(segment.getOrigin(),newOrigin);
        assertEquals(segment.getDestination(),newDestination);
        assertEquals(segment.getName(),"Boulevard Belateche");
    }


}