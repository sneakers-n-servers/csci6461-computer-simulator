package edu.gw.csci.simulator.exceptions;

public class IllegalTrapCode extends SimulatorException {

    public static final int OP_CODE = 2;

    public IllegalTrapCode(String message) {
        super(message);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
