package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.services.BoundingRectangle;
import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * CityMap is model class that represents the city we load from xml files into the application.
 * A city is represented by a list of intersections and segments that connect intersections.
 */
public class CityMap implements Cloneable {

    private Map<Long, Intersection> intersections;
    private Map<Pair<Long, Long>, Segment> segments;


    /**
     * Constructor of CityMap. Inputs are Map Objects that represent our intersections and segments.
     * @param intersections the intersections of the city map
     * @param segments the segments of the city map
     */
    public CityMap(Map<Long, Intersection> intersections, Map<Pair<Long, Long>, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    /**
     * Constructor of an empty CityMap.
     */
    public CityMap() {
        this.intersections = new HashMap<Long, Intersection>();
        this.segments = new HashMap<Pair<Long, Long>, Segment>();
    }

    /**
     * Allows us to clone our CityMap Object.
     * @return a clone of the CityMap object that calls this function.
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone()
            throws CloneNotSupportedException {
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

    /**
     * Adds a single intersection to the CityMap
     * @param intersection of type Intersection
     * @throws ExceptionXML
     */
    public void add(Intersection intersection) throws ExceptionXML {
        Long id = intersection.getId();
        if (intersections.containsKey(id)) {
            throw new ExceptionXML("Error when reading file : Double intersection exception");
        }
        intersections.put(id, intersection);
    }

    /**
     * Adds a list of intersections to the CityMap
     * @param intersections : a List of Intersection
     * @throws ExceptionXML
     */
    public void addAllIntersections(List<Intersection> intersections) throws ExceptionXML {
        for (Intersection intersection : intersections) {
            add(intersection);
        }
    }

    /**
     * Adds a list of segments to the CityMap
     * @param segments : a List of Segment
     * @throws ExceptionXML
     */
    public void addAllSegments(List<Segment> segments) throws ExceptionXML {
        for (Segment segment : segments) {
            add(segment);
        }
    }

    /**
     * Adds a single segment to the CityMap
     * @param segment : a Segment object
     * @throws ExceptionXML
     */
    public void add(Segment segment) throws ExceptionXML {
        Pair<Long, Long> id = segment.getId();
        segments.put(id, segment);
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
