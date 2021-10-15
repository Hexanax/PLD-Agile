package fr.insalyon.pldagile.model;

import java.util.List;
import java.util.Objects;

public class CityMap {
    private List<Intersection> intersections;
    private List<Segment> segments;

    public CityMap(List<Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityMap)) return false;
        CityMap cityMap = (CityMap) o;
        return Objects.equals(getIntersections(), cityMap.getIntersections()) && Objects.equals(getSegments(), cityMap.getSegments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIntersections(), getSegments());
    }
}
