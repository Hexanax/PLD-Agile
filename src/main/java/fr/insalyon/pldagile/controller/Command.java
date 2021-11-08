package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.tsp.ExceptionCityMap;

public interface Command {

    /**
     * Execute the command this
     */
    void doCommand() throws ExceptionCityMap;

    /**
     * Execute the reverse command of this
     */
    void undoCommand() throws ExceptionCityMap;

    void editRequestDuration(int pickupDuration, int deliveryDuration) throws ExceptionCityMap;
}
