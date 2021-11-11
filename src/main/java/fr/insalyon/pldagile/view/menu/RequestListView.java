package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.controller.ListOfCommands;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.MouseListener;
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

        //TODO Move elsewhere? => render()
        //Add request list view to the provided grid pane
        gridPane.add(requestList, 0, 3, 2, 1);
        requestList.getStyleClass().add("requests-list");
        requestList.setPrefWidth(Double.POSITIVE_INFINITY);
        requestList.setOrientation(Orientation.VERTICAL);
        requestList.setOnMouseClicked(RequestMouseListener::mouseClicked);
    }

    public void clear() {
        addressItems.clear();
    }

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

    public String[] getEditableDuration() {
        if (pickupObserver != null && deliveryObserver != null) {
            return new String[]{pickupObserver.getValue(), deliveryObserver.getValue()};
        }
        return null;
    }

    public void setRequestMapView(RequestMapView view) {
        this.requestMapView = view;
    }

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

    private void activeHoverEvent() {
        for (AddressItem item : addressItems) {
            item.setOnMouseEntered(event -> {
                this.setHover(item);
                requestMapView.hoverRequest(item.getRequestNumber());
            });
            item.setOnMouseExited(event -> {
                requestMapView.unHoverRequest(item.getRequestNumber());
            });
        }
    }

    public void setFirstFocus(AddressItem item, int index) {
        requestList.scrollTo(item);
        requestList.getSelectionModel().select(index + 2);
    }

    public void setHover(AddressItem item) {
        requestList.getSelectionModel().select(item);
    }

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

    public void addUpdateCallback(Runnable runnable) {
        updateCallbacks.add(runnable);
    }
}
