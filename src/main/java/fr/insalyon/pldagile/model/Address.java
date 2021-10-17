package fr.insalyon.pldagile.model;

public class Address {
    private Intersection intersection;

    public Address(Intersection intersection) {
        this.intersection = intersection;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

}
