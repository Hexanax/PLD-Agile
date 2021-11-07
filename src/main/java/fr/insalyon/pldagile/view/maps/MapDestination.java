package fr.insalyon.pldagile.view.maps;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapDestination)) return false;
        MapDestination that = (MapDestination) o;
        return getStart().equals(that.getStart()) && getEnd().equals(that.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd());
    }
}
