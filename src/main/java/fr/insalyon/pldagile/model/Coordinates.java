package fr.insalyon.pldagile.model;

import java.util.Objects;

/**
 * Coordinates is a model class that represents a geographical coordinate.
 */
public class Coordinates {

    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Constructor of a coordinate defined by it latitude and longitude
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 && Double.compare(that.getLongitude(), getLongitude()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }
}