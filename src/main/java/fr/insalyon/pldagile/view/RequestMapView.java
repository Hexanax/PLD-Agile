package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.RequestListView;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicReference;

public class RequestMapView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<Node> planningRequestPoints = new PointLayer<>();
    private final Controller controller;
    private PlanningRequest planningRequest;

    private RequestListView requestListView;

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
            depotPoint.setRequestId(-1);
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
                            new RequestMapPin(RequestType.PICKUP, request.getDisplayId())
                    );
                }
                if (delivery != null) {
                    MapPoint mapPoint = new MapPoint(delivery.getIntersection().getCoordinates().getLatitude(), delivery.getIntersection().getCoordinates().getLongitude());
                    mapPoint.setId(delivery.getIntersection().getId());
                    mapPoint.setRequestId(request.getId());
                    mapPoint.setType("delivery");
                    planningRequestPoints.addPoint(
                            mapPoint,
                            new RequestMapPin(RequestType.DELIVERY, request.getDisplayId())
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
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            point.getValue().setOnMouseClicked(event -> {
                if (event.getClickCount() > 1) return; //TODO Check usage
                controller.modifyClick(point.getKey().getRequestId(), point.getKey().getType(), point.getKey().getStepIndex());
            });

            Pair<MapPoint, Node> neighbor = searchneighbor(point);
            point.getValue().setOnMouseEntered(event -> {
                ((Node) event.getTarget()).setScaleX(1.2);
                ((Node) event.getTarget()).setScaleY(1.2);
                if (neighbor != null) {
                    neighbor.getValue().setScaleX(1.2);
                    neighbor.getValue().setScaleY(1.2);
                }
                this.requestListView.setSelected(point.getKey().getRequestId(), point.getKey().getType());
            });

            point.getValue().setOnMouseExited(event -> {
                ((Node) event.getTarget()).setScaleX(1.0);
                ((Node) event.getTarget()).setScaleY(1.0);

                if (neighbor != null) {
                    neighbor.getValue().setScaleX(1.0);
                    neighbor.getValue().setScaleY(1.0);
                }
            });
        }
    }

    private Pair<MapPoint, Node> searchneighbor(Pair<MapPoint, Node> pointHover) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == pointHover.getKey().getRequestId() && pointHover.getKey().getType() != point.getKey().getType()) {
                return point;
            }
        }
        return null;
    }

    @Override
    public void hide() {
        planningRequestPoints.hide();
    }

    @Override
    public void show() {
        planningRequestPoints.show();
    }

    public void setRequestListView(RequestListView view) {
        this.requestListView = view;
    }

    public void hoverRequest(long requestNumber) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == requestNumber) {
                point.getValue().setScaleX(1.2);
                point.getValue().setScaleY(1.2);
            }
        }
    }

    public void unHoverRequest(long requestNumber) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == requestNumber) {
                point.getValue().setScaleX(1.0);
                point.getValue().setScaleY(1.0);
            }
        }
    }
}