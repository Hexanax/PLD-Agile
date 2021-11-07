package fr.insalyon.pldagile.model;


import java.util.Date;

public class Pickup extends Address {

    private int duration;
    private Date arrivalTime;

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