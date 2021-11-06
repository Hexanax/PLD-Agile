package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.*;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RequestMapView implements PropertyChangeListener {

    private final PointLayer<ImageView> planningRequestPoints = new PointLayer();
    private Controller controller;
    private PlanningRequest planningRequest;

    public RequestMapView(Controller controller) {
        this.controller = controller;
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
    }

    public void clear() {
        planningRequestPoints.clearPoints();
    }

    public void render() {
        clear();
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
                //Map points
                MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(pickup.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                planningRequestPoints.addPoint(
                        mapPoint,
                        new RequestMapPin(RequestType.PICKUP)
                );
                mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                mapPoint.setId(delivery.getIntersection().getId());
                mapPoint.setRequestId(request.getId());
                planningRequestPoints.addPoint(
                        mapPoint,
                        new RequestMapPin(RequestType.DELIVERY)
                );
            });
            planningRequestPoints.addPoint(depotPoint, IconProvider.getDepotIcon());
            //TODO Scale it with zoom level
        }
    }

    public MapLayer getLayer() {
        return planningRequestPoints;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.planningRequest = (PlanningRequest) evt.getNewValue();
        render();
    }

    /**
     * Makes request points clickable
     */
    public void activeRequestIntersectionsListener() {
        for (Pair<MapPoint, ImageView> point : planningRequestPoints.getPoints()) {
            point.getValue().setOnMouseClicked(event -> {
                controller.modifyClick(point.getKey().getRequestId(), "Intersection", point.getKey().getStepIndex());
                //System.out.println("active request:" + point.getKey().getStepIndex());
            });
        }
    }

    /**
     * Makes request point non-clickable
     */
    public void disableRequestIntersectionsListener() {
        for (Pair<MapPoint, ImageView> point : planningRequestPoints.getPoints()) {
            point.getValue().setOnMouseClicked(null);
        }
    }

}