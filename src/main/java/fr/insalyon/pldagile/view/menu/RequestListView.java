package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.RequestMapView;
import fr.insalyon.pldagile.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * RequestListView is the lower part of the side panel {@link SidePanelView} and contains all the pickup and delivery
 * details for the tour.
 */
public class RequestListView implements View, PropertyChangeListener {

    private PlanningRequest planningRequest;
    private Tour tour;
    private final ObservableList<AddressItem> addressItems = FXCollections.observableArrayList();
    private final ListView<AddressItem> requestList = new ListView<>();

    private final List<Runnable> updateCallbacks;
    private AddressItem pickupObserver;
    private AddressItem deliveryObserver;
    private RequestMapView requestMapView;

    public RequestListView(GridPane gridPane, Controller controller) {
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        this.tour = controller.getPclTour().getTour();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
        controller.getPclTour().addPropertyChangeListener(this);
        this.updateCallbacks = new ArrayList<>();


        //Add request list view to the provided grid pane
        gridPane.add(requestList, 0, 3, 2, 1);
        requestList.getStyleClass().add("requests-list");
        requestList.setPrefWidth(Double.POSITIVE_INFINITY);
        requestList.setOrientation(Orientation.VERTICAL);
        requestList.setOnMouseClicked(RequestMouseListener::mouseClicked);
    }

    /**
     * Clear the list
     */
    public void clear() {
        addressItems.clear();
    }

    /**
     * Render the list without ordering it
     */
    public void renderUnordered() {
        clear();
        for (Request request : planningRequest.getRequests()) {
            Pickup pickup = request.getPickup();
            if (pickup != null) {
                AddressItem pickupItem = new AddressItem(null, pickup.getDuration(), request.getId(), "Pickup", -1, false);
                addressItems.add(pickupItem);
            }
            Delivery delivery = request.getDelivery();
            if (delivery != null) {
                AddressItem deliveryItem = new AddressItem(null, delivery.getDuration(), request.getId(), "Delivery", -1, false);
                addressItems.add(deliveryItem);
            }
        }
        activeHoverEvent();
    }

    /**
     * Render the list after ordering it
     */
    public void renderOrdered() {
        clear();
        Depot depot = tour.getDepot();
        Map<Long, Request> requests = tour.getRequests();
        AddressItem item = new AddressItem(depot.getDepartureTime(), 0, -1, "Depot", 0, false);
        addressItems.add(item);
        int index = 0;
        for (Pair<Long, String> step : tour.getSteps()) {
            if (Objects.equals(step.getValue(), "pickup")) {
                item = new AddressItem(requests.get(step.getKey()).getPickup().getArrivalTime(), requests.get(step.getKey()).getPickup().getDuration(), step.getKey(), "Pickup", index, false);
                addressItems.add(item);
            }
            if (Objects.equals(step.getValue(), "delivery")) {
                item = new AddressItem(requests.get(step.getKey()).getDelivery().getArrivalTime(), requests.get(step.getKey()).getDelivery().getDuration(), step.getKey(), "Delivery", index, false);
                addressItems.add(item);
            }
            index++;
        }
        Date finalDate = new Date((long) (depot.getDepartureTime().getTime() + tour.getTourDuration() * 1000));
        item = new AddressItem(finalDate, 0, -2, "Depot", (index - 1), false);
        addressItems.add(item);
        activeHoverEvent();
    }

    public ObservableList<AddressItem> getAddressItems() {
        return addressItems;
    }

    /**
     * Set the AddressItem selected editable
     * @param editable if it has to be editable or not
     * @param id the id of the item
     */
    public void makeLastRequestAddedEditable(boolean editable, long id) {
        for (AddressItem item : addressItems) {
            if (item.getRequestNumber() == id) {
                item.setEditable(editable);
                if (editable) {
                    if (item.getType() == "Pickup") {
                        pickupObserver = item;
                    } else {
                        deliveryObserver = item;
                    }
                } else {
                    pickupObserver = null;
                    deliveryObserver = null;
                }
            }
        }
    }

    /**
     * get the time during the item is editable
     * @return an array of string with the durations of pickup and delivery items
     */
    public String[] getEditableDuration() {
        if (pickupObserver != null && deliveryObserver != null) {
            return new String[]{pickupObserver.getValue(), deliveryObserver.getValue()};
        }
        return null;
    }

    public void setRequestMapView(RequestMapView view) {
        this.requestMapView = view;
    }

    /**
     * Set the AddressItem selected
     * @param requestId item id
     * @param type type of addressItem
     */
    public void setSelected(long requestId, String type) {
        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        int index = 0;
        for (AddressItem item : addressItems) {
            if (item.getRequestNumber() == requestId && Objects.equals(item.getType(), type)) {
                this.setFirstFocus(item, index);
            }
            index++;
        }
    }

    /**
     * Hover the requests corresponding to the address items hovered by the mouse
     */
    private void activeHoverEvent() {
        for (AddressItem item : addressItems) {
            item.setOnMouseEntered(event -> {
                this.setHover(item);
                requestMapView.hoverRequest(item.getRequestNumber());
            });
            item.setOnMouseExited(event -> requestMapView.unHoverRequest(item.getRequestNumber()));
        }
    }

    /**
     * Scroll to the item in parameter
     * @param item
     * @param index
     */
    public void setFirstFocus(AddressItem item, int index) {
        requestList.scrollTo(item);
        requestList.getSelectionModel().select(index );
    }

    public void setHover(AddressItem item) {
        requestList.getSelectionModel().select(item);
    }

    /**
     * Render the item
     */
    @Override
    public void render() {
        System.out.println("Address list size = " + addressItems.size());
        //Dynamically compute the list's height
        double listHeight = 0;
        for (AddressItem addressItem : addressItems) {
            addressItem.enforceHeight();
            listHeight += addressItem.getAddressItemHeight();
        }
        requestList.setPrefHeight(listHeight * 1.1 + 50);
        requestList.setItems(addressItems);
        updateCallbacks.forEach(Runnable::run);
    }

    /**
     * Update the RequestList event
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Request list view update");
        String propertyName = evt.getPropertyName();
        if (Objects.equals(propertyName, "tourUpdate")) {
            this.tour = (Tour) evt.getNewValue();
            if (this.tour.getPath() != null) {
                renderOrdered();
            }
        }
        if (Objects.equals(propertyName, "planningRequestUpdate")) {
            this.planningRequest = (PlanningRequest) evt.getNewValue();
            renderUnordered();
        }
        render();
    }

    /**
     * Update the ListView callback
     * @param runnable
     */
    public void addUpdateCallback(Runnable runnable) {
        updateCallbacks.add(runnable);
    }
}
