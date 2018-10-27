package edu.gw.csci.simulator.exceptions;

public class IllegalMemoryAccess extends SimulatorException {

    public IllegalMemoryAccess(String message) {
        super(message);
    }

    @Override
    int getOpcode() {
        return 1;
    }
}
