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
    protected Object clone()
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
        return new Coordinates(latitudeCentral*180/Math.PI,longitudeCentral*180/Math.PI);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMap cityMap = (CityMap) o;
        return Objects.equals(intersections, cityMap.intersections) && Objects.equals(segments, cityMap.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intersections, segments);
    }
}
