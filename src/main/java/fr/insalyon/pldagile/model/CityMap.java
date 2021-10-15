package fr.insalyon.pldagile.model;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class CityMap {
    private Map<Long, Intersection> intersections;
    private Map<Long, Intersection> segments;

    public CityMap(Map<Long, Intersection> intersections, Map<Long, Intersection> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(Map<Long, Intersection> intersections) {
        this.intersections = intersections;
    }
}
