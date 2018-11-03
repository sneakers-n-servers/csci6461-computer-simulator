package edu.gw.csci.simulator.exceptions;

public class IllegalMemoryAccess extends SimulatorException {

    public static final int OP_CODE = 1;

    public IllegalMemoryAccess(String message) {
        super(message);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
