package fr.insalyon.pldagile.tsp;

import java.util.List;

public interface Graph {
    /**
     * @return the number of vertices in <code>this</code>
     */
    int getNbVertices();

    /**
     * @param originId      : Long
     * @param destinationId : Long
     * @return the cost of arc (originId,destinationId) if (originId,destinationId)
     *         is an arc; -1 otherwise
     */
    Double getCost(Long originId, Long destinationId);

    /**
     * @param originId      : Long
     * @param destinationId : Long
     *
     * @return true if <code>(originId,destinationId)</code> is an arc of
     *         <code>this.graph</code>
     */
    boolean isArc(Long originId, Long destinationId);

    /**
     *
     * @param originId the id of the origin vertex
     * @param destinationId the id of the destination vertex
     * @return returns the cost of the shortest path between two vertices, returns -1 if there are no paths between the two
     */
    Double getShortestPathCost(Long originId, Long destinationId);

    /**
     *
     * @param originId the id of the origin vertex
     * @param destinationId the id of the destination vertex
     * @return returns the segments that make the shortest path between two vertices, if the list is empty there are no paths between those vertices
     */
    List<Long> getShortestPath(Long originId, Long destinationId);


}
