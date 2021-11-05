package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.PointLayer;

public class CityMapView {

    private final PointLayer cityPointLayer = new PointLayer();
    private Controller controller;

    public CityMapView(Controller controller) {
        this.controller = controller;
    }

//    public void activeMapIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : cityPointLayer.get) {
//            point.getValue().setOnMouseClicked(event-> {
//                controller.modifyClick(point.getKey().getId(),"Intersection", -1);
//            });
//        }
//    }
//
//    public void disableMapIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : intersectionPoints) {
//            point.getValue().setOnMouseClicked(null);
//        }
//    }

}