package fr.insalyon.pldagile.services;

import fr.insalyon.pldagile.services.CityMapGraph;

import javafx.util.Pair;

import java.util.*;

import static java.lang.Double.POSITIVE_INFINITY;

/**
 * The Dijkstra class stores all the data calculated from applying the Dijkstra algorithm on a CityMapGraph from a specific
 * originId.
 * The distances from origin are stored as a <code>Map&#60;Long,Double&#60;</code>.
 * The predecessors are stored as a <code>Map&#60;Long,Long&#62;</code>
 */
public class Dijkstra {

    //We'll store our data in 4 structures
    private Map<Long, Double> distancesFromOrigin;

    //Stores the predecessor of each vertice id
    private Map<Long, Long> predecessor;
    //Maps a vertice ID to an integer such that { 0 => white, 1 => grey, 2 => black}
    private Map<Long, Integer> color;
    //Represents "grey" vertices, ie vertices visited, but we're not sure if we've found the shortest path yet
    private Map<Long,Double> grey;

    //Graph of our cityMap
    private CityMapGraph cityMapGraph;
    private Long originId;



    /**
     * Initializes a Dijkstra object that will, given a cityMapGraph and an origin ID,
     * contain the distance from origin for each intersection, and the best path to get to this intersection
     * @param cityMapGraph the graph of our city
     * @param originId the origin id of the algorithm
     */
    public Dijkstra(CityMapGraph cityMapGraph, Long originId){
        this.cityMapGraph = cityMapGraph;
        this.originId = originId;

        distancesFromOrigin = new HashMap<>();
        predecessor = new HashMap<>();
        color = new HashMap<>();
        grey = new HashMap<>();

        Set<Long> vertexIds = cityMapGraph.getVertexIds();

        for (Long v : vertexIds) {
            distancesFromOrigin.put(v, POSITIVE_INFINITY);
            predecessor.put(v, null);
            color.put(v, 0);
        }

        distancesFromOrigin.put(originId, 0D);
        //color origin in grey
        color.put(originId,1);
        //and add it in the grey hashmap.
        grey.put(originId,0D);
        runDijkstra();

    }

    /**
     * Implementation of Dijkstra's algorithm based on 3IF-AAIA course by Christine Solnon
     * @return
     */
    private void runDijkstra() {

        Map<String, Object> returnedObject = new HashMap<>();

        while(grey.size() != 0)
        {
            //Get the index of the closest grey vertex
            Long ref = searchMin(grey);

            ArrayList<Pair<Long, Double>> adjacentVertices = cityMapGraph.getGraph().get(ref);
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

//        Currently, we have 2 hashmaps with :
//        * all predecessors
//        * all distances from origin
//        So our dijkstra algorithm has found the real "cost" between to vertices and we can implement TSP.

    }

    /**
     * @param grey a hashmap containing all the vertices currently colored in grey
     * @return the id of the vertex in the grey hashmap such that distance from origin is minimal
     */
    private Long searchMin(Map<Long,Double> grey)
    {
        Double min = POSITIVE_INFINITY;
        Long ref = null;
        Iterator it = grey.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long,Double> entry = (Map.Entry)it.next();
            if(entry.getValue()<min)
            {
                min = entry.getValue();
                ref = entry.getKey();
            }
        }
        return ref;
    }

    /**
     * Stores the order from destination to origin using the predecessor data structure,
     * then reverses this order to get the best path found from origin to destination
     * @param destinationId
     * @return
     */
    public List<Long> getShortestPath(Long destinationId){
        List<Long> shortestPath = new ArrayList<>();
        shortestPath.add(destinationId);
        Long currentID = destinationId;
        while (!Objects.equals(currentID, originId))
        {
            currentID = predecessor.get(currentID);
            if (currentID==null){
                break;
            }
            shortestPath.add(currentID);
        }

        Collections.reverse(shortestPath);

        return shortestPath;
    }

    /**
     * @param destinationId
     * @return cost of the shortest path to go from this.originId to the destinationId
     */
    public Double getShortestPathCost(Long destinationId){
        return distancesFromOrigin.get(destinationId);
    }

    /**
     *
     * @return a hashmap containing all distances from this.originId
     */
    public Map<Long, Double> getDistancesFromOrigin() {
        return distancesFromOrigin;
    }

    /**
     *
     * @return originId of the Dijkstra instance
     */
    public Long getOriginId() {
        return originId;
    }


}
