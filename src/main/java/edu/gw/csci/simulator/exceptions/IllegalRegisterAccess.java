package edu.gw.csci.simulator.exceptions;

public class IllegalRegisterAccess extends SimulatorException {

    public static final int OP_CODE = 5;

    public IllegalRegisterAccess(String message) {
        super(message);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
