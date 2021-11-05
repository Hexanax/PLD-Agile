package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.maps.PointLayer;

public class RequestMapView {

    private final PointLayer planningRequestPoints = new PointLayer();
    private Controller controller;

    public RequestMapView(Controller controller) {
        this.controller = controller;
    }

//    public void activeRequestIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : requestPoints) {
//            point.getValue().setOnMouseClicked(event-> {
//                controller.modifyClick(point.getKey().getRequestId(),"Intersection", point.getKey().getStepIndex());
//                //System.out.println("active request:" + point.getKey().getStepIndex());
//            });
//        }
//    }
//
//    public void disableRequestIntersectionsListener() {
//        for (Pair<MapPoint, Node> point : requestPoints) {
//            point.getValue().setOnMouseClicked(null);
//        }
//    }

}