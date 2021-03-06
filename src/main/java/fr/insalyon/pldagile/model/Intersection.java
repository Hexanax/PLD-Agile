package fr.insalyon.pldagile.model;

import java.util.Objects;

/**
 * Intersection is a model class that represents an intersection of a city map.
 */
public class Intersection {

    private Long id;
    private Coordinates coordinates;

    /**
     * Constructor of an intersection defined by an id and coordinates
     * @param id the id of the intersection
     * @param coordinates the geographical coordinates
     */
    public Intersection(Long id, Coordinates coordinates) {
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

    @Override
    public String toString() {
        return "Intersection: "+ id;
    }
}
