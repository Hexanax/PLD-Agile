package fr.insalyon.pldagile.observer;

import fr.insalyon.pldagile.model.Tour;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PCLTour {

    private Tour tour;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public PCLTour(Tour tour){
        this.tour = tour;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        //System.out.println("notification : " + this.tour + " " + tour);
        //System.out.println(support.getPropertyChangeListeners());
        support.firePropertyChange("tourUpdate", this.tour, tour);
        this.tour = tour;
    }
}
