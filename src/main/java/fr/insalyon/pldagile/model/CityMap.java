package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.xml.ExceptionXML;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

//TODO : switch to hashMap

public class CityMap {
    private Map<Long, Intersection> intersections;
    private Map<Long, Intersection> segments;

    public CityMap(Map<Long, Intersection> intersections, Map<Long, Intersection> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(Map<Long, Intersection> intersections) {
        this.intersections = intersections;
    }

    public void add(Intersection intersection) throws ExceptionXML {
        Long id = intersection.getId();
        if(intersections.containsKey(id)){
            throw new ExceptionXML("Error when reading file : Double id exception");
        }
        intersections.put(id,intersection);
    }
}
