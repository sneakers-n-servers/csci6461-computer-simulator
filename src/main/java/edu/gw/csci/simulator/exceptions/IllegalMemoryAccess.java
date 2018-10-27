package edu.gw.csci.simulator.exceptions;

public class IllegalMemoryAccess extends SimulatorException {

    private int code = 1;

    public IllegalMemoryAccess(String message) {
        
        super(message);
    }
}
