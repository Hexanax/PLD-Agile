package fr.insalyon.pldagile.model;

import java.util.Objects;

public class Intersection {

    private long id;
    private Coordinates coordinates;

    public Intersection(long id, Coordinates coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        Intersection that = (Intersection) o;
        return getId() == that.getId() && Objects.equals(getCoordinates(), that.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCoordinates());
    }
}
