package fr.insalyon.pldagile.tsp;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * CityMapGraphNode represents a vertex (intersection) in the CityMapGraph along with the nodes that are adjacent to it
 * and the cost to reach them.
 *
 * This class is inspired by the Node class in https://www.baeldung.com/java-dijkstra
 */
public class CityMapGraphNode {
    private List<CityMapGraphNode> shortestPath = new LinkedList<>();

    private Integer cost = Integer.MAX_VALUE;

    Map<CityMapGraphNode, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(CityMapGraphNode destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    // getters and setters
}
