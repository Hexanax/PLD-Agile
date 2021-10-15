package fr.insalyon.pldagile.model;

import java.util.Objects;

public class Segment {

    private String name;
    private double length;
    private Intersection origin;
    private Intersection destination;

    public Segment(String name, double length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

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
}
