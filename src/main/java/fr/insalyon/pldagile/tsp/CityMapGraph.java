package fr.insalyon.pldagile.tsp;

import fr.insalyon.pldagile.model.CityMap;
import fr.insalyon.pldagile.model.Intersection;
import fr.insalyon.pldagile.model.Segment;

import javafx.util.Pair;

import java.util.*;

import static java.lang.Double.POSITIVE_INFINITY;

public class CityMapGraph implements Graph {

    private final int nbVertices;
    private final Map<Long, Segment> segments;
    private final Set<Long> vertexIds;
    public static Double NO_ARC_COST = -1D;
    private List<Long> pathIds;


    /*
        Map< vertexId, ArrayList<Pair<adjacentVertexId, length>>>
        "Adjacency list", for each Intersection, the list contains a pair of (origin
        and length between origin and destination)
        */
    private final Map<Long, ArrayList<Pair<Long, Double>>> graph;

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

    @Override
    public Double getShortestPathCost(Long originId, Long destinationId) {
        // Algorithm inspired by https://www.baeldung.com/java-dijkstra
        // Set up the map which stores the distances of nodes from the originId
        Map<Long, Double> distancesFromOrigin = new HashMap<>();
        for (Long v : this.vertexIds) {
            distancesFromOrigin.put(v, POSITIVE_INFINITY);
        }
        // the origin is at a distance of 0 from itself
        distancesFromOrigin.put(originId, 0D);

        // Create the list of unsettled and settled vertices and add the originId as unsettled
        Set<Long> settledVertices = new HashSet<>();
        Set<Long> unsettledVertices = new HashSet<>();
        unsettledVertices.add(originId);

        // While the unsettled nodes set is not empty we:
        while (unsettledVertices.size() != 0) {
            // Choose an evaluation node from the unsettled nodes set, the evaluation node should be the one with the lowest distance from the source.
            Long currentVertexId = getNextUnsettledVertex(unsettledVertices, distancesFromOrigin);
            /*
                As we are using Dijkstra, a Greedy Algorithm, currentVertexId is part of the solution path
             */
            pathIds.add(currentVertexId);
            unsettledVertices.remove(currentVertexId);
            settledVertices.add(currentVertexId);
            if (currentVertexId == null) {
                break;
            }
            // Calculate new distances to direct neighbors by keeping the lowest distance at each evaluation.
            Double costToCurrentVertex;
            List<Pair<Long, Double>>  adjacencyPairs = this.getGraph().get(currentVertexId);
            if (adjacencyPairs == null){
                continue;
            }
            for (Pair<Long, Double> adjacencyPair : adjacencyPairs) {
                Long adjacentVertexId = adjacencyPair.getKey();
                if (!settledVertices.contains(adjacentVertexId)) {
                    costToCurrentVertex = distancesFromOrigin.get(currentVertexId);
                    updateMinimumDistance(costToCurrentVertex, adjacencyPair, distancesFromOrigin);

                    // Add neighbors that are not yet settled to the unsettled nodes set.
                    unsettledVertices.add(adjacentVertexId);
                }
            }
        }
        return distancesFromOrigin.get(destinationId);
    }

    private Long getNextUnsettledVertex(Set<Long> unsettledVertices, Map<Long, Double> distancesFromOrigin) {
        Long lowestDistanceVertexId = null;
        Double lowestDistance = POSITIVE_INFINITY;

        for (Long vertexID : unsettledVertices) {
            if (distancesFromOrigin.get(vertexID) < lowestDistance) {
                lowestDistance = distancesFromOrigin.get(vertexID);
                lowestDistanceVertexId = vertexID;
            }
        }
        return lowestDistanceVertexId;
    }

    private void updateMinimumDistance(Double costToCurrentVertex, Pair<Long, Double> adjacencyPair, Map<Long, Double> distancesFromOrigin) {
        Long adjacentVertexId = adjacencyPair.getKey();
        Double edgeDistance = adjacencyPair.getValue();
        // compute the new edge cost
        Double newWeight = costToCurrentVertex + edgeDistance;
        // if the new cost is lower, update it
        if( newWeight < distancesFromOrigin.get(adjacentVertexId)){
            distancesFromOrigin.replace(adjacentVertexId, newWeight);
        }
    }


    @Override
    public List<Long> getShortestPath(Long originId, Long destinationId) {
        getShortestPathCost(originId,destinationId);
        return getPathIds();
    }

    public List<Long> getPathIds() {
        return pathIds;
    }

}
