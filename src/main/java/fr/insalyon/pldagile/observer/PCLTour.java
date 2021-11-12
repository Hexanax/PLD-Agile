package fr.insalyon.pldagile.observer;

import fr.insalyon.pldagile.model.Tour;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *  Allows a view to subscribe to an object that notifies it when the tour changes
 */
public class PCLTour {

    private Tour tour;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public PCLTour() {}
    public PCLTour(Tour tour){
        this.tour = tour;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener("tourUpdate",pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        ////System.out.println("notification : " + this.tour + " " + tour);
        support.firePropertyChange("tourUpdate", null, tour); //TODO Check null old value, problem in UNDO deleteRequest
        this.tour = tour;
    }
}
