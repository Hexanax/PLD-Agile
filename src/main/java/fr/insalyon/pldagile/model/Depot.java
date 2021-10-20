package fr.insalyon.pldagile.model;

import java.util.Date;

public class Depot extends Address {

    private Date departureTime;

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
}
