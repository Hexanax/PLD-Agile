package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.xml.ExceptionXML;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO : switch to hashMap

public class CityMap {

    private Map<Long, Intersection> intersections;
    private Map<Pair<Long, Long>, Segment> segments;

    public CityMap(Map<Long, Intersection> intersections, Map<Pair<Long, Long>, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public CityMap() {
        this.intersections = new HashMap<Long, Intersection>();
        this.segments = new HashMap<Pair<Long,Long>, Segment>();
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



    public void addAll(List<Segment> segments) throws ExceptionXML {
        for (Segment segment : segments) {
            add(segment);
        }
    }

    public void add(Segment segment) throws ExceptionXML {
        Pair<Long,Long> id = segment.getId();
        segments.put(id, segment);
    }
}
