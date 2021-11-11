package fr.insalyon.pldagile.xml;

import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;
import fr.insalyon.pldagile.view.maps.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class Way {

    private String name;
    private List<Segment> segments;
    private double totalLength;
    private Intersection origin;
    private Intersection destination;

    public Way(List<Segment> segments) {
        this.segments = segments;
        totalLength = 0;
        if(!segments.isEmpty()){
            origin = segments.get(0).getOrigin();
            destination = segments.get(segments.size()-1).getDestination();
            name = segments.get(0).getName();
            calculateLength();
        }
    }
    public Way(Segment segment){
        this.segments = new ArrayList<>();
        totalLength = segment.getLength();
        origin = segment.getOrigin();
        destination = segment.getDestination();
        name = segment.getName();
    }

    public Way() {

    }

    private void calculateLength() {
        for(Segment segment : segments){
            totalLength += segment.getLength();
        }
    }

    public double getTotalLength() {
        return totalLength;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void addSegment(Segment segment){
        if(segment != null){
            if(name.length()<=0) name = segment.getName();
            segments.add(segment);
            totalLength += segment.getLength();
        }

        else System.out.println("segment is null");
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

    public String getName() {
        return name;
    }
}
