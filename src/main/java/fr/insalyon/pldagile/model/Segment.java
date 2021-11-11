package fr.insalyon.pldagile.model;

import javafx.util.Pair;

import java.util.Objects;

/**
 * Segment is a model class that represents a segment of a city map.
 * A segment corresponds to a road between two intersections
 */
public class Segment {

    private Pair<Long, Long> id;
    private String name;
    private double length;
    private Intersection origin;
    private Intersection destination;

    /**
     * Constructor of a segment defined by a name, an origin intersection, a destination intersection
     * and a length
     * @param name the name
     * @param length the length
     * @param origin the origin intersection
     * @param destination the destination intersection
     */
    public Segment(String name, double length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        this.destination = destination;
        this.id = new Pair<>(origin.getId(),destination.getId());
    }

    public String getName() {
        return name;
    }

    public Pair<Long, Long> getId() { return id; }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Segment)) return false;
        Segment segment = (Segment) o;
        return Double.compare(segment.getLength(), getLength()) == 0 && Objects.equals(getName(), segment.getName()) && Objects.equals(getOrigin(), segment.getOrigin()) && Objects.equals(getDestination(), segment.getDestination());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLength(), getOrigin(), getDestination());
    }

    @Override
    public String toString() {
        return "Segment: "+origin.getId() + " -> "+ destination.getId()+ "| meters : "+ length;
    }
}
