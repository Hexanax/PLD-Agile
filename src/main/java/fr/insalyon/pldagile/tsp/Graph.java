package fr.insalyon.pldagile.tsp;

public interface Graph {
    /**
     * @return the number of vertices in <code>this</code>
     */
    public abstract int getNbVertices();

    /**
     * @param originId : Long
     * @param destinationId : Long
     * @return the cost of arc (originId,destinationId) if (originId,destinationId) is an arc; -1 otherwise
     */
    public abstract Double getCost(Long originId, Long destinationId);

    /**
     * @param originId : Long
     * @param destinationId : Long
     *
     * @return true if <code>(originId,destinationId)</code> is an arc of <code>this.graph</code>
     */
    public abstract boolean isArc(Long originId, Long destinationId);

}


