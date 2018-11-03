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
    public IllegalRegisterAccess(String message, boolean runRoutine) {
        super(message, runRoutine);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
