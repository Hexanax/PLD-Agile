package fr.insalyon.pldagile.model;

/**
 * Address is a model class that represents an address.
 * It can be in our model, a pickup, a depot or a delivery
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        if (intersection == null) {
            return other.intersection == null;
        } else return intersection.equals(other.intersection);
    }
}
