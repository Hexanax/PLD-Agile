package fr.insalyon.pldagile.model;

public abstract class Address {
    private long id;
    private Intersection intersection;

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
