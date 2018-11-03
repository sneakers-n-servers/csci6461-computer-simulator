package edu.gw.csci.simulator.exceptions;


/**
 * This exception is raised when a value is attempting to be converted
 * to another value, but is innapropriate for storage. For instance
 * trying to convert a non-binary string to an integer.
 */
public class IllegalValue extends SimulatorException {

    public static final int OP_CODE = 3;

    public IllegalValue(String mess) {
        super(mess);
    }
    public IllegalValue(String message, boolean runRoutine) {
        super(message, runRoutine);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
