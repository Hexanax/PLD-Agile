package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.xml.ExceptionXML;

import java.util.HashMap;
import java.util.Map;

//TODO : switch to hashMap

public class CityMap {

    private Map<Long, Intersection> intersections;
    private Map<Long, Segment> segments;

    public CityMap(Map<Long, Intersection> intersections, Map<Long, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public CityMap() {
        this.intersections = new HashMap<Long, Intersection>();
        this.segments = new HashMap<Long, Segment>();
    }

    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public Map<Long, Segment> getSegments() {
        return segments;
    }

    public void add(Intersection intersection) throws ExceptionXML {
        Long id = intersection.getId();
        if (intersections.containsKey(id)) {
            throw new ExceptionXML("Error when reading file : Double intersection exception");
        }
        intersections.put(id, intersection);
    }

    static int index = 1;

    public void add(Segment segment) throws ExceptionXML {
        Long id = (long) index++;
//        if(segments.containsValue(segment)){
//            throw new ExceptionXML("Error when reading file: Double segment exception");
//        }
        //verif pas de segment sans origine/dest
        segments.put(id, segment);
    }
}
