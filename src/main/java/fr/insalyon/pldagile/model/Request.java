package fr.insalyon.pldagile.model;

public class Request {

    private Long id;
    private Pickup pickup;
    private Delivery delivery;

    public Request(Pickup pickup, Delivery delivery) {
        this.pickup = pickup;
        this.delivery = delivery;

        //by default id is set to null - Use with the tour
        this.id = null;
    }

    public Pickup getPickup() {
        return pickup;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    /**
     * The actual ID of the request is not as readable for the user and must be
     * adapted. This method provides an adapted Id for the views.
     * 
     * @return The Id of the request that is meant to be displayed
     */
    public Long getDisplayId() {
        return this.id + 1;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

}
