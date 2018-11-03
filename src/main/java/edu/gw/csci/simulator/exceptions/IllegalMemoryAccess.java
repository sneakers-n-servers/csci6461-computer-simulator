package edu.gw.csci.simulator.exceptions;


/**
 * This exception is raised when a reserved memory location is accessed
 * by an instruction.
 */
public class IllegalMemoryAccess extends SimulatorException {

    public static final int OP_CODE = 1;

    public IllegalMemoryAccess(String message) {
        super(message);
    }
    public IllegalMemoryAccess(String message, boolean runRoutine) {
        super(message, runRoutine);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
