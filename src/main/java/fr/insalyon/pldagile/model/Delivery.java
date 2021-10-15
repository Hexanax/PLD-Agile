package fr.insalyon.pldagile.model;

public class Delivery extends Address {
    private int duration;

    public Delivery(Intersection intersection,int duration) {
        super(intersection);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

