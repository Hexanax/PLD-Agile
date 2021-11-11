package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Coordinates;
import fr.insalyon.pldagile.model.Intersection;

import java.util.Map;

/**
 * Utility class that computes data from a CityMap.
 * Contains methods that allow us to compute the center of a CityMap,
 * the coordinates of a x <a href = https://en.wikipedia.org/wiki/Minimum_bounding_rectangle#:~:text=The%20minimum%20bounding%20rectangle%20(MBR,)%2C%20max(y).>Minimum Bounding Rectangle</a>,
 * and the optimal zoom value to have a full view of the CityMap.
 */
public class CityMapUtils {

    /**
     * Calculates the central coordinates of the map based on the intersections loaded from the XML file
     * @return coordinates of the center of our CityMap
     */
    public static Coordinates getCenter(CityMap cityMap) {
        Map<Long, Intersection> intersections = cityMap.getIntersections();

        double totalCoordinates = 0;
        double latitudeIntermed = 0;
        double longitudeIntermed = 0;
        double z = 0;
        for (Intersection intersection : intersections.values()) {

            // Convert each pair lat/long to a 3D vector
            double latitude = intersection.getCoordinates().getLatitude() * Math.PI / 180;
            double longitude = intersection.getCoordinates().getLongitude() * Math.PI / 180;

            // Sum the vectors
            latitudeIntermed += Math.cos(latitude) * Math.cos(longitude);
            longitudeIntermed += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);

        }
        totalCoordinates = intersections.values().size();

        // Normalize the resulting vector
        latitudeIntermed = latitudeIntermed / totalCoordinates;
        longitudeIntermed = longitudeIntermed / totalCoordinates;
        z = z / totalCoordinates;

        // Reconvert to spherical coordinates
        double longitudeCentral = Math.atan2(longitudeIntermed, latitudeIntermed);
        double squareCentral = Math.sqrt(latitudeIntermed * latitudeIntermed + longitudeIntermed * longitudeIntermed);
        double latitudeCentral = Math.atan2(z, squareCentral);

        // Create the central coordinates
        //System.out.println("center : " + (latitudeCentral * 180 / Math.PI) + " long" + (longitudeCentral * 180 / Math.PI));
        return new Coordinates(latitudeCentral * 180 / Math.PI, (longitudeCentral * 180 / Math.PI) + 0.005);

    }


    /**
     * Computes the minimum bounding rectangle for our CityMap and returns it.
     * @return min/max values for latitude/longitude, corresponding to a minimum bounding rectangle for
     * the current CityMap
     */
    public static BoundingRectangle getMinimumBoundingRectangle(CityMap cityMap) {

        Map<Long, Intersection> intersections = cityMap.getIntersections();

        double minLatitude = intersections.values().iterator().next().getCoordinates().getLatitude();
        double minLongitude = intersections.values().iterator().next().getCoordinates().getLongitude();
        double maxLatitude = intersections.values().iterator().next().getCoordinates().getLatitude();
        double maxLongitude = intersections.values().iterator().next().getCoordinates().getLongitude();

        for (Intersection intersection : intersections.values()) {
            // Convert each pair lat/long to a 3D vector
            double longitude = intersection.getCoordinates().getLongitude();
            double latitude = intersection.getCoordinates().getLatitude();
            minLatitude = Math.min(latitude, minLatitude);
            minLongitude = Math.min(longitude, minLongitude);
            maxLatitude = Math.max(latitude, maxLatitude);
            maxLongitude = Math.max(longitude, maxLongitude);
        }

        return new BoundingRectangle(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }


    /**
     * converts latitude to radians
     * @param lat a double representing our latitude in degrees
     * @return a double representing our latitude in radians
     */
    private static double latRad(double lat) {
        double sin = Math.sin(lat * Math.PI / 180);
        double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
        return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
    }

    /**
     * Calculates the optimal zoom value to display our map, so that every intersection
     * of our CityMap is shown.
     * @return optimal zoom value to display the minimum bounding rectangle
     */
    public static double getOptimalZoom(CityMap cityMap) {
        BoundingRectangle boundingRectangle = getMinimumBoundingRectangle(cityMap);
        double minLatitude = boundingRectangle.getMinLatitude();
        double minLongitude = boundingRectangle.getMinLongitude();
        double maxLatitude = boundingRectangle.getMaxLatitude();
        double maxLongitude = boundingRectangle.getMaxLongitude();

        ////System.out.println("minLong"+minLongitude+ "minLat"+ minLatitude+ "maxLong"+ maxLongitude+ "maxLat"+maxLatitude);
        double latDif = Math.abs(latRad(maxLatitude) - latRad(minLatitude));
        double longDif = Math.abs(maxLongitude - minLongitude);

        double latFrac = latDif / Math.PI;
        double longFrac = longDif / 360;

        double latZoom = Math.log(1 / latFrac) / Math.log(2);
        double longZoom = Math.log(1 / longFrac) / Math.log(2);

        ////System.out.println("longZoom"+longZoom+"latZoom"+latZoom);
        return Math.min(longZoom, latZoom);
    }
}
