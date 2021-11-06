package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.LineLayer;
import fr.insalyon.pldagile.view.maps.MapLayer;
import fr.insalyon.pldagile.view.maps.MapPoint;
import fr.insalyon.pldagile.view.maps.PointLayer;
import fr.insalyon.pldagile.view.menu.RequestItem;
import fr.insalyon.pldagile.view.menu.RequestMenuView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class RequestMapView implements PropertyChangeListener {


    private final PointLayer planningRequestPoints = new PointLayer();
    private Controller controller;
    private PlanningRequest planningRequest;

    public RequestMapView(Controller controller) {
        this.controller = controller;
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
    }

    public void clearRequest() {
        planningRequestPoints.clearPoints();
    }

    public void render() {
        PlanningRequest planningRequest = this.planningRequest;
        if (!planningRequest.getRequests().isEmpty() && planningRequest.getDepot() != null) {
            // Render the planning request
            Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
            MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
            depotPoint.setId(planningRequest.getDepot().getIntersection().getId());
            planningRequest.getRequests().forEach(request -> {
                // Items in list
                Pickup pickup = request.getPickup();
                Delivery delivery = request.getDelivery();
                RequestItem deliveryItem = new RequestItem("Delivery at " + request.getDelivery().getIntersection().getId(), "Duration: " + request.getDelivery().getDuration(), request.getId(), "Delivery", -1);
                //Map points
                MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(pickup.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                planningRequestPoints.addPoint(
                        mapPoint,
                        IconProvider.getPickupIcon()
                );
                mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(delivery.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                planningRequestPoints.addPoint(
                        mapPoint,
                        IconProvider.getDropoffIcon()
                );
            });
            planningRequestPoints.addPoint(depotPoint,  IconProvider.getDepotIcon());
            //TODO Scale it with zoom level
        }
    }

    public MapLayer getLayer() {
        return planningRequestPoints;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        System.out.println(propertyName);

        if (propertyName.equals("planningRequestUpdate")){
            clearRequest();
            this.planningRequest = (PlanningRequest) evt.getNewValue();
            render();

        }
    }

    public void orderListRequests(ArrayList<Pair<Long, String>> steps, Map<Long, Request> requests, Depot depot) {
        planningRequestPoints.clearPoints();
        ArrayList<RequestItem> items = new ArrayList<>();
        int index = 0;
        for (Pair<Long, String> step : steps) {
            if (Objects.equals(step.getValue(), "pickup")) {
                double mapPointLatitude = requests.get(step.getKey()).getPickup().getIntersection().getCoordinates().getLatitude();
                double mapPointLongitude = requests.get(step.getKey()).getPickup().getIntersection().getCoordinates().getLongitude();
                MapPoint mapPoint = new MapPoint(mapPointLatitude, mapPointLongitude);
                mapPoint.setId(requests.get(step.getKey()).getPickup().getIntersection().getId());
                mapPoint.setRequestId(requests.get(step.getKey()).getId());
                mapPoint.setStepIndex(index);
                planningRequestPoints.addPoint(
                        mapPoint,
                        IconProvider.getPickupIcon()
                );
            }
            if (Objects.equals(step.getValue(), "delivery")) {
                double mapPointLatitude = requests.get(step.getKey()).getDelivery().getIntersection().getCoordinates().getLatitude();
                double mapPointLongitude = requests.get(step.getKey()).getDelivery().getIntersection().getCoordinates().getLongitude();
                MapPoint mapPoint = new MapPoint(mapPointLatitude, mapPointLongitude);
                mapPoint.setId(requests.get(step.getKey()).getDelivery().getIntersection().getId());
                mapPoint.setRequestId(requests.get(step.getKey()).getId());
                mapPoint.setStepIndex(index);
                planningRequestPoints.addPoint(
                        mapPoint,
                        IconProvider.getDropoffIcon()
                );
            }

            index++;
        }

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