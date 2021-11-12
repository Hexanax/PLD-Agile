package fr.insalyon.pldagile.view.maps;

/**
 * This class defines a MapDestination composed of two {@link MapPoint}
 * A starting point and an ending point
 */
public class MapDestination {

    private final MapPoint start;
    private final MapPoint end;

    /**
     * Creates a map Destination component based on the start and end {@link MapPoint}
     * @param start start {@link MapPoint}
     * @param end {@link MapPoint}
     */
    public MapDestination(MapPoint start, MapPoint end) {
        this.start = start;
        this.end = end;
    }

    public MapPoint getStart() {
        return start;
    }

    public MapPoint getEnd() {
        return end;
    }

}