package fr.insalyon.pldagile.model;

/**
 * Request is a model class that represents a request.
 * A request is identified by its ID and represents a Pickup location where
 * our driver must pick up a package as well as a delivery location where our
 * driver must deliver our package.
 */
public class Request {

    private Long id;
    private final Pickup pickup;
    private Delivery delivery;

    /**
     * Constructor of an intersection defined by a pickup and a delivery
     * @param pickup
     * @param delivery
     */
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Request other = (Request) obj;
        if (delivery == null) {
            if (other.delivery != null)
                return false;
        } else if (!delivery.equals(other.delivery))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (pickup == null) {
            return other.pickup == null;
        } else return pickup.equals(other.pickup);
    }

}
