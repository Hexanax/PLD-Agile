package fr.insalyon.pldagile.controller;

public interface Command {

    /**
     * Execute the command this
     */
    void doCommand();

    /**
     * Execute the reverse command of this
     */
    void undoCommand();

    void editRequestDuration(int pickupDuration, int deliveryDuration);
}
