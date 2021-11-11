package fr.insalyon.pldagile.view.maps;

/**
 * This class defines a MapDestination composed of two @see MapPoint
 * MapPoint start, MapPoint end
 */
public class MapDestination {

    private MapPoint start;
    private MapPoint end;

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