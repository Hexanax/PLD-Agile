package fr.insalyon.pldagile.model;

import java.util.Date;

/**
 * Depot is a model class that represents a depot. It is a type of address.
 * A depot is the address where the deliverer will start and finish a tour
 */
public class Depot extends Address {

    private Date departureTime;

    /**
     * Constructor of a depot defined by an intersection and the departure time of a tour
     * @param intersection the intersection on the city map
     * @param departureTime the departure time for the tour
     */
    public Depot(Intersection intersection, Date departureTime) {
        super(intersection);
        this.departureTime = departureTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Depot: \n " + super.toString();
    }
}
