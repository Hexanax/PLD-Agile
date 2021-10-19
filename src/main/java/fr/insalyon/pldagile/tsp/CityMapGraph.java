package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMapGraph implements Graph {

    private int nbVertices;
    private Map<Long, Intersection> intersections;
    private Map<Long, Segment> segments;

    // private Map<Pair<Long, Long>, Double> graph;

    // Map< destinationId, ArrayList<Pair<originId, length>>>
    // "Adjacency list", for each Intersection, the list contains a pair of (origin and length between origin and destination)
    private Map<Long, ArrayList<Pair<Long, Double>>> graph;

    public CityMapGraph(CityMap cityMap){
        intersections = cityMap.getIntersections();
        segments = cityMap.getSegments();
        nbVertices = intersections.size();
        graph = new HashMap<Long, ArrayList<Pair<Long,Double>>>();
        buildGraph();
    }

    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public Map<Long, Segment> getSegments() {
        return segments;
    }

    public Map<Long, ArrayList<Pair<Long, Double>>> getGraph() {
        return graph;
    }

    /**
     *  Generates the graph using the segments of a CityMap
     */
    private void buildGraph(){
        Long originId;
        Long destinationId;
        Double length;
        Pair<Long,Long> pair;
        for(Segment s : segments.values() ) {
            originId = s.getOrigin().getId();
            destinationId = s.getDestination().getId();
            length = s.getLength();

            // Add adjacency of intersections
            pair = new Pair<Long, Long>(originId,destinationId);
            graph.put(pair, length);
        }
    }
    
    @Override
    public int getNbVertices() {
        // It doesn't always correspond to the actual number of connected vertices
        return nbVertices;
    }

    @Override
    public Double getCost(Long originId, Long destinationId) {
        Pair<Long, Long> searched = new Pair<>(originId,destinationId);
        Double cost  = graph.get(searched);
        return cost==null ? -1 : cost ;
    }

    @Override
    public boolean isArc(Long originId,Long destinationId) {
        Pair<Long, Long> searched = new Pair<>(originId,destinationId);
        boolean isArc = graph.containsKey(searched);
        return isArc;
    }
}
