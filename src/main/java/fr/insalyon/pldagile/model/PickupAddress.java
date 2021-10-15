package fr.insalyon.pldagile.model;


public class PickupAddress extends Address {

    private long duration;

    public PickupAddress(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
