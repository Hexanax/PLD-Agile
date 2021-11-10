package fr.insalyon.pldagile.model;

/**
 * Address is a model class that represents an address.
 */
public class Address {

    private Intersection intersection;

    public Address(Intersection intersection) {
        this.intersection = intersection;
    }

    public Address(){}

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }


    @Override
    public String toString() {
        return intersection.toString();
    }
}
