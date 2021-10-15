package fr.insalyon.pldagile.model;

public class Request {

    private Long id;
    private PickupAddress pickupAddress;
    private DeliveryAddress deliveryAddress;

    public Request(PickupAddress pickupAddress, DeliveryAddress deliveryAddress) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;

        // generate id later
    }
}
