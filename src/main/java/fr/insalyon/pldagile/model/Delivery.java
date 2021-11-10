package fr.insalyon.pldagile.model;

import java.util.Date;

/**
 * Delivery is a model class that represents a delivery. It is a type of address.
 * A delivery is the address where the deliverer will finish a request
 */
public class Delivery extends Address {

    private int duration;
    private Date arrivalTime;

    /**
     * Constructor of a delivery defined by an intersection and the duration to deliverer the package
     * @param intersection the intersection on the city map
     * @param duration the duration
     */
    public Delivery(Intersection intersection, int duration) {
        super(intersection);
        this.duration = duration;
        this.arrivalTime = null;
    }

    public Delivery(){}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setArrivalTime(int time) {
        arrivalTime=new Date();
        arrivalTime.setTime(time);
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}