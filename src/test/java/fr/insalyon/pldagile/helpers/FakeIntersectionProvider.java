package fr.insalyon.pldagile.helpers;

import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;

import java.util.List;

public class FakeIntersectionProvider {
    static public Intersection getFakeIntersection(){
        return new Intersection(1L, new Coordinates(45.5, 4.8));
    }

    static public List<Intersection> getFakeIntersections(){
        Intersection[] intersections = {
                new Intersection(1L, new Coordinates(45.5, 4.8)),
                new Intersection(2L, new Coordinates(45.6, 4.9)),
                new Intersection(3L, new Coordinates(45.7, 5)),
                new Intersection(4L, new Coordinates(45.5, 5)),
                new Intersection(5L, new Coordinates(45.6, 5)),
                new Intersection(6L, new Coordinates(45.8, 4.8)),
                new Intersection(7L, new Coordinates(45.8, 5)),
                new Intersection(8L, new Coordinates(45.9, 5.1)),
                new Intersection(9L, new Coordinates(45.1, 5.2)),

        };
        return List.of(intersections);
    }
}
