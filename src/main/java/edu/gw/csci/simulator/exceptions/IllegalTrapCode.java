package edu.gw.csci.simulator.exceptions;


/**
 * This exception is raised when the trap error code, and therefore
 * routie is unrecognized by the simulator.
 */
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
