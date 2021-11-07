package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.view.Colors;
import fr.insalyon.pldagile.view.IconProvider;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.util.Objects;

public class DirectedLine {

    private MapDestination mapDestination;
    private Line line;
    private ImageView direction;

    public DirectedLine(MapDestination mapDestination) {
        this.mapDestination = mapDestination;
        line = new Line();
        line.setStroke(Colors.getTourLineColor());
        line.setStrokeWidth(4);
        DropShadow borderEffect = new DropShadow(BlurType.THREE_PASS_BOX, Colors.getTourLineStrokeColor(), 2, 1, 0, 0);
        line.setEffect(borderEffect);
        direction = IconProvider.getDirectionIcon(20);
    }

    public MapDestination getMapDestination() {
        return mapDestination;
    }

    public void setDirection(ImageView icon){
        direction = icon;
    }

    public Line getLine() {
        return line;
    }

    public ImageView getDirection() {
        return direction;
    }

    /**
        This method allows comparing two DirectedLines in order to verifying their presence on the map
        @return 0 if the parameter is has the same mapDestination. 1 if their MapDestinations are inverted (one's start
        is equal to other's end). -1 otherwise.

     */
    /*
    @Override
    public int compareTo(Object o) {
        if (this == o ) return 0;
        if (!(o instanceof DirectedLine)) return -1;
        int directionChange;
        DirectedLine that = (DirectedLine) o;
        if(this.mapDestination.equals(that.mapDestination)) {
            directionChange = 0;
        }else{
            // if the start and the end are inverted, directionChange > 0, else directionChange <0;
            directionChange = !(this.mapDestination.getStart().equals(that.mapDestination.getEnd())) ?
                    -1 : !(this.mapDestination.getEnd().equals(that.mapDestination.getStart())) ? -1 : 1;
        }

        return directionChange;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedLine)) return false;
        DirectedLine that = (DirectedLine) o;
        return Objects.equals(getMapDestination(), that.getMapDestination());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMapDestination());
    }
}
