package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;

import javafx.util.Pair;

import java.util.*;

import static java.lang.Double.POSITIVE_INFINITY;

public class CityMapGraph implements Graph {

    private final int nbVertices;
    private final Map<Long, Intersection> intersections;
    private final Map<Long, Segment> segments;
    public static Double NO_ARC_COST = -1D;

    /*
    Map< originId, ArrayList<Pair<destinationId, length, segmentId>>>
    "Adjacency list", for each Intersection, the list contains a pair of (origin
    and length between origin and destination)
    */
    private final Map<Long, ArrayList<Pair<Long, Double>>> graph;

    public CityMapGraph(CityMap cityMap) {
        intersections = cityMap.getIntersections();
        segments = cityMap.getSegments();
        nbVertices = intersections.size();
        graph = new HashMap<Long, ArrayList<Pair<Long, Double>>>();
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
     * Generates the graph using the segments of a CityMap
     */
    private void buildGraph() {
        Long originId;
        Long destinationId;
        Double length;
        Pair<Long, Double> adjacentVertex;
        ArrayList<Pair<Long, Double>> adjacentVertices;
        for (Segment s : segments.values()) {
            originId = s.getOrigin().getId();
            destinationId = s.getDestination().getId();
            length = s.getLength();
            adjacentVertex = new Pair<Long, Double>(destinationId, length);

            // Load the pre-existing adjacent vertices if they exist
            adjacentVertices = graph.containsKey(originId) ? graph.get(originId) : new ArrayList<Pair<Long, Double>>();
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

    @Override
    public Double getShortestPathCost(Long originId, Long destinationId) {
        // Algorithm inspired by https://www.baeldung.com/java-dijkstra
        Double cost = 0D;
        Set<Long> allVertices = this.getGraph().keySet();

        // Set up the map which stores the distances of nodes from the originId
        Map<Long, Double> distancesFromOrigin = new HashMap<>();
        for (Long v : allVertices){
            distancesFromOrigin.put(v, POSITIVE_INFINITY);
        }
        // the origin is at a distance of 0 from itself
        distancesFromOrigin.put(originId,0D);

        // Create the list of unsettled and settled vertices and add the originId as unsettled
        Set<Long> settledVertices = new HashSet<>();
        Set<Long> unsettledVertices = new HashSet<>();
        unsettledVertices.add(originId);
        //                While the unsettled nodes set is not empty we:
        //        Choose an evaluation node from the unsettled nodes set, the evaluation node should be the one with the lowest distance from the source.
        //                Calculate new distances to direct neighbors by keeping the lowest distance at each evaluation.
        //                Add neighbors that are not yet settled to the unsettled nodes set.
        while (unsettledVertices.size() != 0) {
            Long currentVertex = getLowestDistanceNode(unsettledVertices);
            unsettledVertices.remove(currentVertex);
            for (Pair < Long, Double> adjacencyPair:
                    this.getGraph().get(currentVertex)) {
                Long adjacentVertex = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();
                if (!unsettledVertices.contains(adjacentVertex)) {
                    calculateMinimumDistance(adjacentVertex, edgeWeight, currentVertex);
                    unsettledVertices.add(adjacentVertex);
                }
            }
            settledVertices.add(currentVertex);
        }
        return cost;
    }

    private Long getLowestDistanceNode(Set<Long> unsettledVertices){
        return (Long)unsettledVertices.toArray()[0];
    }


    @Override
    public List<Segment> getShortestPath(Long originId, Long destinationId) {
        return null;
    }



}
