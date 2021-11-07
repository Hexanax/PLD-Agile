package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.*;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RequestMapView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<ImageView> planningRequestPoints = new PointLayer<>();
    private final Controller controller;
    private PlanningRequest planningRequest;

    public RequestMapView(Controller controller) {
        this.controller = controller;
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
    }

    @Override
    public void render() {
        planningRequestPoints.clearPoints();
        PlanningRequest planningRequest = this.planningRequest;
        if (!planningRequest.getRequests().isEmpty() && planningRequest.getDepot() != null) {
            // Render the planning request
            Coordinates depotCoordinates = planningRequest.getDepot().getIntersection().getCoordinates();
            MapPoint depotPoint = new MapPoint(depotCoordinates.getLatitude(), depotCoordinates.getLongitude());
            depotPoint.setId(planningRequest.getDepot().getIntersection().getId());
            depotPoint.setType("depot");
            planningRequest.getRequests().forEach(request -> {
                // Items in list
                Pickup pickup = request.getPickup();
                Delivery delivery = request.getDelivery();
                //Map points
                if (pickup != null) {
                    MapPoint mapPoint = new MapPoint(pickup.getIntersection().getCoordinates().getLatitude(), pickup.getIntersection().getCoordinates().getLongitude());
                    mapPoint.setId(pickup.getIntersection().getId());
                    mapPoint.setRequestId(request.getId());
                    mapPoint.setType("pickup");
                    planningRequestPoints.addPoint(
                            mapPoint,
                            new RequestMapPin(RequestType.PICKUP, request.getId())
                    );
                }
                if (delivery != null) {
                    MapPoint mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                    mapPoint.setId(delivery.getIntersection().getId());
                    mapPoint.setRequestId(request.getId());
                    mapPoint.setType("delivery");
                    planningRequestPoints.addPoint(
                            mapPoint,
                            new RequestMapPin(RequestType.DELIVERY, request.getId())
                    );
                }
            });
            planningRequestPoints.addPoint(depotPoint, new DepotMapPin());
        }
        activeRequestIntersectionsListener();
    }

    public MapLayer getLayer() {
        return planningRequestPoints;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("RequestMapView event " + evt);
        this.planningRequest = (PlanningRequest) evt.getNewValue();
        render();
    }

    /**
     * Makes request points clickable
     */
    //TODO Always listen for the request points click and just handle it depending on the test in the controller/state
    public void activeRequestIntersectionsListener() {
        for (Pair<MapPoint, ImageView> point : planningRequestPoints.getPoints()) {
            point.getValue().setOnMouseClicked(event -> {
                controller.modifyClick(point.getKey().getRequestId(), point.getKey().getType(), point.getKey().getStepIndex());
            });
        }
    }

    //TODO Inspect usage and maybe improve
    public PointLayer<ImageView> getPlanningRequestPoints() {
        return planningRequestPoints;
    }

    @Override
    public void hide() {
        planningRequestPoints.hide();
    }

    @Override
    public void show() {
        planningRequestPoints.show();
    }

}