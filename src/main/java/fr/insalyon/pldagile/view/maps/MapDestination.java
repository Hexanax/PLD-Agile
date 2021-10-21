package fr.insalyon.pldagile.view.maps;

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
