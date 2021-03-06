package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;

import javafx.util.Pair;

import java.util.*;

/**
 * The class CityMapGraph is a set of vertices and arcs that represents our CityMap.
 * The nodes are the intersections of our CityMap and the arcs are the segments of our CityMap.
 * It implements the Graph Interface.
 */
public class CityMapGraph implements Graph {

    private final int nbVertices;
    private final Map<Pair<Long, Long>,Segment> segments;
    private final Set<Long> vertexIds;
    public static Double NO_ARC_COST = -1D;
    private final List<Long> pathIds;

    /**
        "Adjacency list", for each Intersection, the list contains a pair of (origin
        and length between origin and destination)
        */
    private final Map<Long, ArrayList<Pair<Long, Double>>> graph;


    /**
     * Constructor generating a graph from a CityMap
     * @param cityMap the CityMap we want to build the graph from.
     */
    public CityMapGraph(CityMap cityMap) {
        Map<Long, Intersection> intersections = cityMap.getIntersections();
        segments = cityMap.getSegments();
        nbVertices = intersections.size();
        vertexIds = new HashSet<>(intersections.keySet());
        graph = new HashMap<>();
        pathIds = new ArrayList<>();
        buildGraph();
    }

    public Map<Long, ArrayList<Pair<Long, Double>>> getGraph() {
        return graph;
    }

    /**
     * Method used to generate the graph using the segments of a CityMap
     */
    private void buildGraph() {
        Long originId;
        long destinationId;
        double length;
        Pair<Long, Double> adjacentVertex;
        ArrayList<Pair<Long, Double>> adjacentVertices;
        for (Segment s : segments.values()) {
            originId = s.getOrigin().getId();
            destinationId = s.getDestination().getId();
            length = s.getLength();
            adjacentVertex = new Pair<>(destinationId, length);

            // Load the pre-existing adjacent vertices if they exist
            adjacentVertices = graph.containsKey(originId) ? graph.get(originId) : new ArrayList<>();
            adjacentVertices.add(adjacentVertex);

            // update the graph with the new values
            if (graph.containsKey(originId)) {
                graph.replace(originId, adjacentVertices);

            } else {
                graph.put(originId, adjacentVertices);
            }

        }
    }

    @Override
    public int getNbVertices() {
        // It doesn't always correspond to the actual number of connected vertices
        return nbVertices;
    }

    @Override
    public Double getCost(Long originId, Long destinationId) {
        // If the vertex leads to no nodes return -1
        if (!graph.containsKey(originId)) {
            return NO_ARC_COST;
        }
        ArrayList<Pair<Long, Double>> adjacentVertices = graph.get(originId);
        // Find the cost of traveling to the destination vertex
        // Note: this process could be made faster by storing the adjacent vertices using a map
        for (Pair<Long, Double> a : adjacentVertices) {
            if (a.getKey().equals(destinationId)) {
                return a.getValue();
            }
        }
        return NO_ARC_COST;
    }

    @Override
    public boolean isArc(Long originId, Long destinationId) {
        return !(Objects.equals(this.getCost(originId, destinationId), NO_ARC_COST));
    }


    public List<Long> getPathIds() {
        return pathIds;
    }

    public Set<Long> getVertexIds() {
        return vertexIds;
    }

}