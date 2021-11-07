package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import fr.insalyon.pldagile.view.RequestMapView;
import fr.insalyon.pldagile.view.maps.MapPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class RequestListView extends Region implements PropertyChangeListener {


    private final Controller controller;
    private PlanningRequest planningRequest;
    private Tour tour;
    private final ObservableList<AddressItem> addressItems = FXCollections.observableArrayList();

    private MouseListener mouseListener;

    private AddressItem pickupObserver;
    private AddressItem deliveryObserver;
    private RequestMapView requestMapView;
    private RequestView requestView;

    public RequestListView(Controller controller) {
        this.controller = controller;
        this.planningRequest = controller.getPclPlanningRequest().getPlanningRequest();
        controller.getPclPlanningRequest().addPropertyChangeListener(this);
        this.tour = controller.getPclTour().getTour();
        controller.getPclTour().addPropertyChangeListener(this);

        mouseListener = new MouseListener(controller);
    }

    public void clear(){
        addressItems.clear();
    }

    public void renderUnordered(){
        clear();
        for(Request request : planningRequest.getRequests()){
            Pickup pickup = request.getPickup();
            if(pickup != null){
                AddressItem pickupItem = new AddressItem(null, pickup.getDuration(), request.getId(), "Pickup",-1, false);
                addressItems.add(pickupItem);
            }
            Delivery delivery = request.getDelivery();
            if(delivery != null){
                AddressItem deliveryItem = new AddressItem(null, delivery.getDuration(), request.getId(), "Delivery",-1,false);
                addressItems.add(deliveryItem);
            }


        }
        activeHoverEvent();
    }

    public void renderOrdered(){
        clear();
        Depot depot = tour.getDepot();
        Map<Long, Request> requests = tour.getRequests();


        AddressItem item = new AddressItem(depot.getDepartureTime(), 0, -1, "Depot",0, false);
        addressItems.add(item);
        int index = 0;
        for(Pair<Long, String > step : tour.getSteps()){
            if(Objects.equals(step.getValue(), "pickup"))
            {
                item = new AddressItem(requests.get(step.getKey()).getPickup().getArrivalTime(), requests.get(step.getKey()).getPickup().getDuration(), step.getKey(), "Pickup",index, false);
                addressItems.add(item);
            }
            if(Objects.equals(step.getValue(), "delivery")){
                item = new AddressItem(requests.get(step.getKey()).getDelivery().getArrivalTime(), requests.get(step.getKey()).getDelivery().getDuration(), step.getKey(),"Delivery",index, false);
                addressItems.add(item);
            }
            index++;
        }

        Date finalDate = new Date((long) (depot.getDepartureTime().getTime() + tour.getTourDuration()*1000));
        item = new AddressItem(finalDate, 0, -2,"Depot",(index-1),false);
        addressItems.add(item);
        activeHoverEvent();

    }



    public ObservableList<AddressItem> getAddressItems(){
        return addressItems;
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(Objects.equals(propertyName, "tourUpdate")){
            this.tour = (Tour) evt.getNewValue();
            if(this.tour.getPath()!=null) {
                renderOrdered();
            }
        }
        if(Objects.equals(propertyName, "planningRequestUpdate")){
            this.planningRequest = (PlanningRequest) evt.getNewValue();
            renderUnordered();
        }
    }


    public void makeLastRequestAddedEditable(boolean editable , long id) {
        for(AddressItem item : addressItems){
            if(item.getRequestNumber() == id){
                item.setEditable(editable);
                if(editable) {
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

    public String[] getEditableDuration(){
        if(pickupObserver !=null && deliveryObserver !=null){
            return new String[]{pickupObserver.getValue(), deliveryObserver.getValue()};
        }
        return null;
    }

    public void setRequestMapView(RequestMapView view) {
        this.requestMapView = view;
    }

    public void setSelected(long requestId, String type) {
        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        int index=0;
        for(AddressItem item : addressItems){
            if(item.getRequestNumber()==requestId && Objects.equals(item.getType(), type)){
                requestView.setFirstFocus(item, index);
            }
            index++;
        }
    }

    private void activeHoverEvent() {
        for(AddressItem item : addressItems){
            item.setOnMouseEntered(event->{
                requestView.setHover(item);
                requestMapView.hoverRequest(item.getRequestNumber());
            });
            item.setOnMouseExited(event->{
                requestMapView.unHoverRequest(item.getRequestNumber());

            });

        }
    }


    public void setRequestView(RequestView requestView) {
        this.requestView = requestView;
    }
}
