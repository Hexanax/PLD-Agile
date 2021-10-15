package fr.insalyon.pldagile.model;

public class DepotAddress extends Address{
    private long departureTime;

    public DepotAddress(long departureTime) {
        this.departureTime = departureTime;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }
}
