package edu.gw.csci.simulator.exceptions;

/**
 * This exception occurs when trying to retrieve a general purpose
 * or index register higher than what the simulator supports.
 */
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
