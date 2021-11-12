package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;

import java.util.ArrayList;
import java.util.List;


/**
 * Way is a class that represents a road composed by a list of segments having the same name.
 *
 */

public class Way {

    private String name;
    private final List<Segment> segments;
    private double totalLength;
    private Intersection origin;
    private Intersection destination;


    /**
     * Constructor of Way.
     * Constructs a Way by adding all the segments on the list
     * @param segments a list containing the segments of the way
     */

    public Way(List<Segment> segments) {
        this.segments = segments;
        totalLength = 0;
        if(!segments.isEmpty()){
            origin = segments.get(0).getOrigin();
            destination = segments.get(segments.size()-1).getDestination();
            name = segments.get(0).getName();

            if(name.equals("")) name = " road ";
            calculateLength();
        }
    }

    /**
     * Constructor of Way.
     * Constructs a Way by adding the first segment of the way. The name "road" is added by default.
     * @param segment the first segment of the way
     */
    public Way(Segment segment){
        this.segments = new ArrayList<>();
        segments.add(segment);

        totalLength = segment.getLength();
        origin = segment.getOrigin();
        destination = segment.getDestination();

        name = segment.getName();
        if(name.equals("")) name = " road ";
    }

    /**
     * Constructor of an empty Way.
     */
    public Way() {
        segments = new ArrayList<>();

        name = "";

        totalLength = 0;
    }

    /**
     *  Calculates the length of the way by iterating through the segments list.
     */

    private void calculateLength() {
        for(Segment segment : segments){
            totalLength += segment.getLength();
        }
    }

    public String getName() {
        return name;
    }

    public double getTotalLength() {
        return totalLength;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    /**
     *  Adds a not nullable segment to the Way. If no name has been attributed to the  Way, the segment's one is attributed.
     */
    public void addSegment(Segment segment){
        if(segment != null){
            if(name.length()<=0) name = segment.getName();
            if(name.equals("")) name = " road ";

            segments.add(segment);
            totalLength += segment.getLength();
            if (origin ==null) origin = segment.getOrigin();
            destination = segment.getDestination();

        }

    }

    @Override
    public String toString() {
        return "Way{" +
                "name='" + name + '\'' +
                ", totalLength=" + totalLength +
                ", origin=" + origin +
                ", destination=" + destination +
                '}';
    }

}
