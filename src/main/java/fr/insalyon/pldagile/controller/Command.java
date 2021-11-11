package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.services.ExceptionCityMap;

/**
 * This interface gives the pattern to add a request or delete a request
 * with the possibility to cancel
 * This interface is based on the example of the 4IF course - Object Oriented Design and AGILE software development by Mrs Solnon
 * <a href="https://moodle.insa-lyon.fr/mod/resource/view.php?id=110978"> Placo source code </a>
 */
public interface Command {

    /**
     * Execute the command this
     */
    void doCommand() throws ExceptionCityMap;

    /**
     * Execute the reverse command of this
     */
    void undoCommand() throws ExceptionCityMap;

    /**
     * Modify duration of a tour after the execution of a command
     * @param pickupDuration pickup duration edited
     * @param deliveryDuration delivery duration edited
     * @throws ExceptionCityMap
     */
    void editRequestDuration(int pickupDuration, int deliveryDuration) throws ExceptionCityMap;
}
