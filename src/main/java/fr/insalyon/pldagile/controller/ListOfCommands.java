package fr.insalyon.pldagile.controller;

import fr.insalyon.pldagile.tsp.ExceptionCityMap;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> list;
    private int currentIndex;

    public ListOfCommands() {
        currentIndex = -1;
        list = new LinkedList<Command>();
    }

    /**
     * Add command c to this
     *
     * @param c the command to add
     */
    public void add(Command c) throws ExceptionCityMap {
        int i = currentIndex + 1;
        while (i < list.size()) {
            list.remove(i);
        }
        currentIndex++;
        list.add(currentIndex, c);
        c.doCommand();
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo)
     */
    public void undo() throws ExceptionCityMap {
        if (currentIndex >= 0) {
            Command cde = list.get(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Permanently remove the last added command (this command cannot be reinserted again with redo)
     */
    public void cancel() throws ExceptionCityMap {
        if (currentIndex >= 0) {
            Command cde = list.get(currentIndex);
            list.remove(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Reinsert the last command removed by undo
     */
    public void redo() throws ExceptionCityMap {
        if (currentIndex < list.size() - 1) {
            currentIndex++;
            Command cde = list.get(currentIndex);
            cde.doCommand();
        }
    }

    /**
     * Permanently remove all commands from the list
     */
    public void reset() {
        currentIndex = -1;
        list.clear();
    }

    public Command getLastCommand() {
        return list.get(currentIndex);
    }
}