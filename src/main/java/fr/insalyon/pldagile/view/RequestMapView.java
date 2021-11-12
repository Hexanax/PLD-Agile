package fr.insalyon.pldagile.view;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.maps.*;
import fr.insalyon.pldagile.view.menu.RequestListView;
import javafx.scene.Node;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;


/**
 * This class stores, displays, hides, highlights and unhighlights the requests' icons on the map,
 * it also makes the connection between the requests' icons and their items in the list view
 */
public class RequestMapView implements PropertyChangeListener, View, Hideable {

    private final PointLayer<Node> planningRequestPoints = new PointLayer<>();
    private final Controller controller;
    private PlanningRequest planningRequest;

    private RequestListView requestListView;

    /**
     * Creates a RequestMapView component
     * @param controller
     */
    public RequestMapView(Controller controller) {
        this.controller = controller;
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
    }

    /**
     * Loops through the planning requests and adds their corresponding icons to {@link PointLayer},
     * ids and request's ids to the created {@link MapPoint}
     * to
     */
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

    /**
     * updates the {@link PlanningRequest} when an event occurs
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.planningRequest = (PlanningRequest) evt.getNewValue();
        render();
    }

    /**
     * Makes the request's delivery and pickup icons bigger when hovering over a single icon
     */
    public void activeRequestIntersectionsListener() {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            point.getValue().setOnMouseClicked(event -> {
                if (event.getClickCount() > 1) return; //TODO Check usage
                controller.modifyClick(point.getKey().getRequestId(), point.getKey().getType(), point.getKey().getStepIndex());
            });

            Pair<MapPoint, Node> neighbor = searchNeighbor(point);
            point.getValue().setOnMouseEntered(event -> {
                ((Node) event.getTarget()).setScaleX(1.2);
                ((Node) event.getTarget()).setScaleY(1.2);
                if (neighbor != null) {
                    neighbor.getValue().setScaleX(1.2);
                    neighbor.getValue().setScaleY(1.2);
                }
                System.out.println("Set selected id = " + point.getKey().getRequestId());
                this.requestListView.setSelected(point.getKey().getRequestId(), point.getKey().getType());
            });

            point.getValue().setOnMouseExited(event -> {
                ((Node) event.getTarget()).setScaleX(point.getKey().getDefaultScale());
                ((Node) event.getTarget()).setScaleY(point.getKey().getDefaultScale());

                if (neighbor != null) {
                    neighbor.getValue().setScaleX(neighbor.getKey().getDefaultScale());
                    neighbor.getValue().setScaleY(neighbor.getKey().getDefaultScale());
                }
            });
        }
    }

    /**
     * looks for the hovered icon's neighbor in the request, a neighbor is either a
     * delivery (or pickup) that is in the same request as a pickup (or delivery)
     * @param pointHover the hovered icon
     * @return
     */
    private Pair<MapPoint, Node> searchNeighbor(Pair<MapPoint, Node> pointHover) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == pointHover.getKey().getRequestId() && pointHover.getKey().getType() != point.getKey().getType()) {
                return point;
            }
        }
        return null;
    }

    /**
     * hides the requests' icons from the map
     */
    @Override
    public void hide() {
        planningRequestPoints.hide();
    }


    /**
     * shows the requests' icons in the map
     */
    @Override
    public void show() {
        planningRequestPoints.show();
    }


    public void setRequestListView(RequestListView view) {
        this.requestListView = view;
    }

    /**
     * highlights the request's 2 icons when hovering the corresponding (same request id) item
     * @param requestNumber request's id
     */
    public void hoverRequest(long requestNumber) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == requestNumber) {
                point.getValue().setScaleX(1.2);
                point.getValue().setScaleY(1.2);
            }
        }
    }


    /**
     * unhighlights the request's 2 icons that correspond to the request id given in entry
     * @param requestNumber request's id
     */
    public void unHoverRequest(long requestNumber) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == requestNumber) {
                point.getValue().setScaleX(point.getKey().getDefaultScale());
                point.getValue().setScaleY(point.getKey().getDefaultScale());
            }
        }
    }

    /**
     * highlights the icon that has the same request id and type as the ones in entry
     * @param id request's id
     * @param type type of the {@link MapPoint} : pickup or delivery
     */
    public void scaleUpAddress(long id, String type) {
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == id && Objects.equals(point.getKey().getType(), type)) {
                point.getKey().setDefaultScale(1.35);
                point.getValue().setScaleX(1.35);
                point.getValue().setScaleY(1.35);
            }

            if (point.getKey().getRequestId() == id && !Objects.equals(point.getKey().getType(), type)) {
                point.getKey().setDefaultScale(1.0);
                point.getValue().setScaleX(1.0);
                point.getValue().setScaleY(1.0);
            }
        }
    }

    /**
     * unhighlights the icon that has the same request id as the one in entry
     * @param id request's id
     */
    public void unScaleUpAddresses(long id){
        for (Pair<MapPoint, Node> point : planningRequestPoints.getPoints()) {
            if (point.getKey().getRequestId() == id ) {
                point.getKey().setDefaultScale(1.0);
                point.getValue().setScaleX(1.0);
                point.getValue().setScaleY(1.0);
            }
        }
    }
}