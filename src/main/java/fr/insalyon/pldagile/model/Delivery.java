package fr.insalyon.pldagile.model;

import java.util.Date;

public class Delivery extends Address {

    private int duration;
    private Date arrivalTime;

    public Delivery(Intersection intersection, int duration) {
        super(intersection);
        this.duration = duration;
        this.arrivalTime = new Date();
    }

    public Delivery(){}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setArrivalTime(int time) {
        arrivalTime.setTime(time);
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}