package fr.insalyon.pldagile.model;

public class DeliveryAddress extends Address {
    private long duration;

    public DeliveryAddress(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

