package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO : switch to hashMap

public class CityMap implements Cloneable {

    private Map<Long, Intersection> intersections;
    private Map<Pair<Long, Long>, Segment> segments;

    private PropertyChangeSupport support;

    public CityMap(Map<Long, Intersection> intersections, Map<Pair<Long, Long>, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
        support = new PropertyChangeSupport(this);
    }

    public CityMap() {
        this.intersections = new HashMap<Long, Intersection>();
        this.segments = new HashMap<Pair<Long,Long>, Segment>();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public Object clone()
            throws CloneNotSupportedException
    {
        return new CityMap(this.intersections, this.segments);
    }


    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public Intersection getIntersection(Long id) {
        return intersections.get(id);
    }

    public Map<Pair<Long, Long>, Segment> getSegments() {
        return segments;
    }

    public void add(Intersection intersection) throws ExceptionXML {
        Long id = intersection.getId();
        if (intersections.containsKey(id)) {
            throw new ExceptionXML("Error when reading file : Double intersection exception");
        }
        intersections.put(id, intersection);
    }

    public void addAllIntersections(List<Intersection> intersections) throws ExceptionXML {
        for (Intersection intersection : intersections) {
            add(intersection);
        }
    }

    public void addAllSegments(List<Segment> segments) throws ExceptionXML {
        for (Segment segment : segments) {
            add(segment);
        }
    }

    public void add(Segment segment) throws ExceptionXML {
        Pair<Long,Long> id = segment.getId();
        segments.put(id, segment);
    }

    public void buildMap(List<Intersection> intersections,List<Segment> segments) throws ExceptionXML, CloneNotSupportedException {
        CityMap oldMap = (CityMap) this.clone();
        addAllIntersections(intersections);
        addAllSegments(segments);
        support.firePropertyChange("mapLoaded", oldMap, this);
    }


    /**
     *
     * @return min/max values for latitude/longitude, corresponding to a minimum bounding rectangle for
     * the current CityMap
     */
    public Map<String, Double> getMinimumBoundingRectangle(){

        double minLongitude = intersections.values().iterator().next().getCoordinates().getLongitude();
        double minLatitude = intersections.values().iterator().next().getCoordinates().getLatitude();
        double maxLongitude = intersections.values().iterator().next().getCoordinates().getLongitude();
        double maxLatitude = intersections.values().iterator().next().getCoordinates().getLatitude();

        Map<String, Double> boundingRectangle = new HashMap<String, Double>();


        for(Intersection intersection : intersections.values()){
            // Convert each pair lat/long to a 3D vector
            double longitude = intersection.getCoordinates().getLongitude();
            double latitude = intersection.getCoordinates().getLatitude();
            minLongitude = (longitude < minLongitude)  ? longitude : minLongitude;
            minLatitude = (latitude < minLatitude)  ? latitude : minLatitude;
            maxLongitude = (longitude > maxLongitude)  ? longitude : maxLongitude;
            maxLatitude = (latitude > maxLatitude)  ? latitude : maxLatitude;
        }
        boundingRectangle.put("minLongitude",minLongitude);
        boundingRectangle.put("minLatitude",minLatitude);
        boundingRectangle.put("maxLongitude",maxLongitude);
        boundingRectangle.put("maxLatitude",maxLatitude);
        return boundingRectangle;
    }

    /**
     * converts latitude to radiants
     * @param lat
     * @return
     */
    private double latRad(double lat){
        double sin = Math.sin(lat * Math.PI/180);
        double radX2 = Math.log((1+sin)/(1-sin))/2;
        return Math.max(Math.min(radX2,Math.PI),-Math.PI)/2;
    }

    /**
     *
     * @return optimal zoom value to display the minimum bounding rectangle
     */
    public double getOptimalZoom(){
        Map<String, Double> boundingRectangle = new HashMap<>(getMinimumBoundingRectangle());
        double minLongitude =boundingRectangle.get("minLongitude");
        double minLatitude =boundingRectangle.get("minLatitude");
        double maxLongitude =boundingRectangle.get("maxLongitude");
        double maxLatitude =boundingRectangle.get("maxLatitude");

        System.out.println("minLong"+minLongitude+ "minLat"+ minLatitude+ "maxLong"+ maxLongitude+ "maxLat"+maxLatitude);
        double latDif = Math.abs(latRad(maxLatitude) - latRad(minLatitude));
        double longDif = Math.abs(maxLongitude - minLongitude);

        double latFrac = latDif/ Math.PI;
        double longFrac = longDif/360;

        double latZoom = Math.log(1/latFrac)/Math.log(2);
        double longZoom = Math.log(1/longFrac)/Math.log(2);

        System.out.println("longZoom"+longZoom+"latZoom"+latZoom);
        return Math.min(longZoom,latZoom);

    }
    /**
     * Calculates the central coordinates of the map based on the intersections loaded from the XML file
     */
    public Coordinates getCenter() {

        double totalCoordinates = 0;
        double latitudeIntermed = 0;
        double longitudeIntermed = 0;
        double z = 0;
        for (Intersection intersection : intersections.values()) {

            // Convert each pair lat/long to a 3D vector
            double latitude = intersection.getCoordinates().getLatitude()*Math.PI / 180;
            double longitude = intersection.getCoordinates().getLongitude()*Math.PI / 180;

            // Sum the vectors
            latitudeIntermed += Math.cos(latitude) * Math.cos(longitude);
            longitudeIntermed += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);

        }
        totalCoordinates = intersections.values().size();

        // Normalize the resulting vector
        latitudeIntermed = latitudeIntermed/totalCoordinates;
        longitudeIntermed = longitudeIntermed/totalCoordinates;
        z = z/totalCoordinates;

        // Reconvert to spherical coordinates
        double  longitudeCentral = Math.atan2(longitudeIntermed, latitudeIntermed);
        double squareCentral = Math.sqrt(latitudeIntermed*latitudeIntermed + longitudeIntermed*longitudeIntermed);
        double latitudeCentral = Math.atan2(z, squareCentral);

        // Create the central coordinates
        System.out.println("center : " + (latitudeCentral*180/Math.PI) + " long" + (longitudeCentral*180/Math.PI));
        return new Coordinates(latitudeCentral*180/Math.PI,(longitudeCentral*180/Math.PI)+0.005);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMap cityMap = (CityMap) o;
        return (intersections.equals(cityMap.intersections)) && (segments.equals(cityMap.segments));
    }

    @Override
    public int hashCode() {
        return Objects.hash(intersections, segments);
    }
}
