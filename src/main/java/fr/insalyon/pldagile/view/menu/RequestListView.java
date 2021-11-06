package fr.insalyon.pldagile.view.menu;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
            AddressItem pickupItem = new AddressItem(new Date(), pickup.getDuration(), request.getId(), "Pickup",-1, false);
            Delivery delivery = request.getDelivery();
            AddressItem deliveryItem = new AddressItem(new Date(), delivery.getDuration(), request.getId(), "Delivery",-1,false);
            addressItems.add(pickupItem);
            addressItems.add(deliveryItem);
        }
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

    }

    public ObservableList<AddressItem> getList(){
        return addressItems;
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        System.out.println("------------");
        System.out.println(propertyName);
        System.out.println("------------");
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
}
