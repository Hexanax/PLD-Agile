package fr.insalyon.pldagile.model;

import java.util.Date;

/**
 * Pickup is a model class that represents a pickup. It is a type of address.
 * A pickup is the address where the deliverer will start a request
 */
public class Pickup extends Address {

    private int duration;
    private Date arrivalTime;

    /**
     * Constructor of a pickup defined by an intersection and the duration to deliverer the package
     * @param intersection the intersection on the city map
     * @param duration the duration
     */
    public Pickup(Intersection intersection, int duration) {
        super(intersection);
        this.duration = duration;
        this.arrivalTime = null;
    }

    public Pickup(){
        super();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setArrivalTime(int time) {
        arrivalTime = new Date();
        arrivalTime.setTime(time);
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}