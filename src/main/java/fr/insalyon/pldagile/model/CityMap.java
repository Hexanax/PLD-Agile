package fr.insalyon.pldagile.model;

import fr.insalyon.pldagile.xml.ExceptionXML;

import java.util.HashMap;
import java.util.List;
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

    public Intersection getIntersection(Long id) {
        return intersections.get(id);
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

    public void addAll(List<Segment> segments) throws ExceptionXML {
        for (Segment segment : segments) {
            add(segment);
        }
    }

    public void add(Segment segment) throws ExceptionXML {
        Long id = (long) index++;
//        if(segments.containsValue(segment)){
//            throw new ExceptionXML("Error when reading file: Double segment exception");
//        }
        //verif pas de segment sans origine/dest
        segments.put(id, segment);
    }


    /**
     * Calculates the central coordinates of the map based on the intersections loaded from the XML file
     * @param intersections
     */
    public Coordinates getCenter(Map<Long, Intersection> intersections) throws ExceptionXML {

        double totalCoordinates = 0;
        double latitudeIntermed = 0;
        double longitudeIntermed = 0;
        double z = 0;
        for (Intersection intersection : intersections.values()) {

            // Convert each pair lat/long to a 3D vector
            double latitude = intersection.getCoordinates().getLatitude()*Math.PI / 180;
            double longitude = intersection.getCoordinates().getLongitude()*Math.PI / 180;

            // sum the vectors
            latitudeIntermed += Math.cos(latitude) * Math.cos(longitude);
            longitudeIntermed += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);

        }
        totalCoordinates = intersections.values().size();

        // normalize the resulting vector
        latitudeIntermed = latitudeIntermed/totalCoordinates;
        longitudeIntermed = longitudeIntermed/totalCoordinates;
        z = z/totalCoordinates;

        // reconvert to spherical coordinates
        double  longitudeCentral = Math.atan2(longitudeIntermed, latitudeIntermed);
        double squareCentral = Math.sqrt(latitudeIntermed*latitudeIntermed + longitudeIntermed*longitudeIntermed);
        double latitudeCentral = Math.atan2(z, squareCentral);

        // Create the central coordinates
        return new Coordinates(latitudeCentral*180/Math.PI,longitudeCentral*180/Math.PI);

    }

}
