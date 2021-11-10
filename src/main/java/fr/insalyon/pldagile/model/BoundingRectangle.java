package fr.insalyon.pldagile.model;

/**
 * The class BoundingRectangle is the minimum bounding rectangle of our CityMap.
 * It represents the coordinates of the corners of a rectangle, so that all intersections in our
 * CityMap are included inside this rectangle.
 */
public class BoundingRectangle {

    private double minLatitude;
    private double minLongitude;
    private double maxLatitude;
    private double maxLongitude;

    /**
     * Constructor of the minimum bounding rectangle.
     * Inputs are the coordinates of the min/max latitude and min/max longitude of our intersections
     *
     * @param minLatitude the smallest latitude of city map
     * @param minLongitude the smallest longitude of city map
     * @param maxLatitude the greatest latitude of the city map
     * @param maxLongitude the greatest latitude of the city map
     */
    public BoundingRectangle(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public boolean isInsideRectangle(Coordinates coords) {
        if (coords.getLatitude() > minLatitude && coords.getLatitude() < maxLatitude &&
                coords.getLongitude() > minLongitude && coords.getLongitude() < maxLongitude) {
            return true;
        }
        return false;
    }
}

