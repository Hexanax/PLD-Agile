package fr.insalyon.pldagile.observer;


import fr.insalyon.pldagile.model.PlanningRequest;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Allows a view to subscribe to an object that notifies it when the planningRequest changes
 */
public class PCLPlanningRequest {

    private PlanningRequest planningRequest;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public PCLPlanningRequest() { }
    public PCLPlanningRequest(PlanningRequest planningRequest) {
        this.planningRequest = planningRequest;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener("planningRequestUpdate",pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public PlanningRequest getPlanningRequest() {
        return planningRequest;
    }

    public void setPlanningRequest(PlanningRequest planningRequest){
        support.firePropertyChange("planningRequestUpdate", null, planningRequest);
        this.planningRequest = planningRequest;
    }

}
