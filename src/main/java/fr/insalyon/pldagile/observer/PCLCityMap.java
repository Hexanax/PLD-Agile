package fr.insalyon.pldagile.observer;

import fr.insalyon.pldagile.model.CityMap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Allows a view to subscribe to an object that will notify them when the cityMap changes
 */
public class PCLCityMap {

    private CityMap cityMap;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public PCLCityMap() { }
    public PCLCityMap(CityMap citymap) {
        this.cityMap = citymap;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener("cityMapUpdate", pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        support.firePropertyChange("cityMapUpdate", this.cityMap, cityMap);
        this.cityMap = cityMap;
    }

}