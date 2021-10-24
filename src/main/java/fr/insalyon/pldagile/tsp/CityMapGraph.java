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

    /**
     * Implementation of Dijkstra's algorithm based on 3IF-AAIA course by Christine Solnon
     * @param originId
     * @param destinationId
     * @return
     */
    public Map<String,Object> Dijkstra(Long originId, Long destinationId) {

        Map<String, Object> returnedObject = new HashMap<>();

        //We'll store our data in 4 structures
        Map<Long, Double> distancesFromOrigin = new HashMap<>();
        Map<Long, Long> predecessor = new HashMap<>();

        //Maps a vertice ID to an integer such that { 0 => white, 1 => grey, 2 => black}
        Map<Long, Integer> color = new HashMap<>();

        //Represents "grey" vertices, ie vertices visited but we're not sure we've found the shortest path yet
        Map<Long,Double> grey = new HashMap<>();


        //initialize the values : all distances to infinity, all predecessors to null, and all vertices "white"
        for (Long v : this.vertexIds) {
            distancesFromOrigin.put(v, POSITIVE_INFINITY);
            predecessor.put(v, null);
            color.put(v, 0);
        }

        distancesFromOrigin.put(originId, 0D);
        //color origin in grey
        color.put(originId,1);
        //and add it in the grey hashmap.
        grey.put(originId,0D);


        while(grey.size() != 0)
        {
            //Get the index of the closest grey vertex
            Long ref = searchMin(grey);

            ArrayList<Pair<Long, Double>> adjacentVertices = graph.get(ref);
            if(adjacentVertices != null){
                for(Pair<Long, Double> pair : adjacentVertices){
                    //if the vertex is not already black (=> we already know the shortest path)
                    if(color.get(pair.getKey()) != 2){
                        if(distancesFromOrigin.get(pair.getKey()) > distancesFromOrigin.get(ref) + pair.getValue()){
                            //release edge
                            distancesFromOrigin.put(pair.getKey(), distancesFromOrigin.get(ref) + pair.getValue());
                            predecessor.put(pair.getKey(), ref);
                        }
                        if(color.get(pair.getKey()) ==0){
                            color.put(pair.getKey(),1);
                            grey.put(pair.getKey(), distancesFromOrigin.get(pair.getKey()));
                        }
                    }
                }
            }
            //color current vertex in black and remove it from grey list
            color.put(ref, 2);
            grey.remove(ref);

        }

//        Currently, we have a hashmap with :
//        * all predecessors
//        * all distances from origin
//        => we need to compute best the path to go from origin to destination

        List<Long> shortestPath = computeShortestPath(predecessor,destinationId,originId);
        returnedObject.put("shortestPath",shortestPath);
        returnedObject.put("shortestPathCost",distancesFromOrigin.get(destinationId));
        System.out.println(returnedObject);
        return returnedObject;


    }

    /**
     * Stores the order from destination to origin using the predecessor data structure,
     * then reverses this order to get the best path found from origin to destination
     * @param predecessor Map<Long,Long> with all predecessors
     * @param destinationId
     * @param originId
     * @return
     */
    public List<Long> computeShortestPath(Map<Long,Long> predecessor, Long destinationId, Long originId){
        List<Long> seq = new ArrayList<>();
        seq.add(destinationId);
        Long currentID = destinationId;
        while (!Objects.equals(currentID, originId))
        {
            currentID = predecessor.get(currentID);
            if (currentID==null){
                break;
            }
            seq.add(currentID);
        }

        Collections.reverse(seq);
        System.out.println(seq.toString());

        return seq;
    }


    /**
     * @param grey a hashmap containing all the vertices currently colored in grey
     * @return the id of the vertex in the grey hashmap such that distance from origin is minimal
     */
    public Long searchMin(Map<Long,Double> grey)
    {
        Double min = POSITIVE_INFINITY;
        Long ref = null;
        Iterator it = grey.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long,Double> entry = (Map.Entry)it.next();
            System.out.println(entry.getKey() + " = " + entry.getValue());
            if(entry.getValue()<min)
            {
                min = entry.getValue();
                ref = entry.getKey();
            }
        }
        return ref;
    }



//    @Override
//    public Double getShortestPathCost(Long originId, Long destinationId) {
//        // Algorithm inspired by https://www.baeldung.com/java-dijkstra
//        // Set up the map which stores the distances of nodes from the originId
//        Map<Long, Double> distancesFromOrigin = new HashMap<>();
//        for (Long v : this.vertexIds) {
//            distancesFromOrigin.put(v, POSITIVE_INFINITY);
//        }
//        // the origin is at a distance of 0 from itself
//        distancesFromOrigin.put(originId, 0D);
//
//        // Create the list of unsettled and settled vertices and add the originId as unsettled
//        Set<Long> settledVertices = new HashSet<>();
//        Set<Long> unsettledVertices = new HashSet<>();
//        unsettledVertices.add(originId);
//
//        // While the unsettled nodes set is not empty we:
//        while (unsettledVertices.size() != 0) {
//            // Choose an evaluation node from the unsettled nodes set, the evaluation node should be the one with the lowest distance from the source.
//            Long currentVertexId = getNextUnsettledVertex(unsettledVertices, distancesFromOrigin);
//            unsettledVertices.remove(currentVertexId);
//            settledVertices.add(currentVertexId);
//            if (currentVertexId == null) {
//                break;
//            }
//            // Calculate new distances to direct neighbors by keeping the lowest distance at each evaluation.
//            Double costToCurrentVertex;
//            List<Pair<Long, Double>> adjacencyPairs = this.getGraph().get(currentVertexId);
//            if (adjacencyPairs == null) {
//                continue;
//            }
//            for (Pair<Long, Double> adjacencyPair : adjacencyPairs) {
//                Long adjacentVertexId = adjacencyPair.getKey();
//                if (!settledVertices.contains(adjacentVertexId)) {
//                    costToCurrentVertex = distancesFromOrigin.get(currentVertexId);
//                    updateMinimumDistance(costToCurrentVertex, adjacencyPair, distancesFromOrigin);
//
//                    // Add neighbors that are not yet settled to the unsettled nodes set.
//                    unsettledVertices.add(adjacentVertexId);
//                }
//            }
//        }
//        return distancesFromOrigin.get(destinationId);
//    }

//    private Long getNextUnsettledVertex(Set<Long> unsettledVertices, Map<Long, Double> distancesFromOrigin) {
//        Long lowestDistanceVertexId = null;
//        Double lowestDistance = POSITIVE_INFINITY;
//
//        for (Long vertexID : unsettledVertices) {
//            if (distancesFromOrigin.get(vertexID) < lowestDistance) {
//                lowestDistance = distancesFromOrigin.get(vertexID);
//                lowestDistanceVertexId = vertexID;
//            }
//        }
//
//        return lowestDistanceVertexId;
//    }
//
//    private boolean updateMinimumDistance(Double costToCurrentVertex, Pair<Long, Double> adjacencyPair, Map<Long, Double> distancesFromOrigin) {
//        Long adjacentVertexId = adjacencyPair.getKey();
//        Double edgeDistance = adjacencyPair.getValue();
//        // compute the new edge cost
//        Double newWeight = costToCurrentVertex + edgeDistance;
//        // if the new cost is lower, update it
//        if (newWeight < distancesFromOrigin.get(adjacentVertexId)) {
//            distancesFromOrigin.replace(adjacentVertexId, newWeight);
//            return true;
//        }
//        return false;
//    }

    @Override
    public List<Long> getShortestPath(Long originId, Long destinationId) {
        return (List) Dijkstra(originId, destinationId).get("shortestPath");

    }


    @Override
    public Double getShortestPathCost(Long originId, Long destinationId) {
        return (Double)Dijkstra(originId, destinationId).get("shortestPathCost");

    }

    public List<Long> getPathIds() {
        return pathIds;
    }

}
