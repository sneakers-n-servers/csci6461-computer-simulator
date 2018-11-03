package edu.gw.csci.simulator.exceptions;

import edu.gw.csci.simulator.cpu.TrapController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Catch-all for exceptions from the application rather than the simulated computer.
 *
 * @version 20180916
 */
public abstract class SimulatorException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger(SimulatorException.class);
    private static TrapController trapController;

    /**
     * This constructor logs the message to the console, and if set, will execute a
     * trap routine via the {@link TrapController}. If the trap controller is not
     * set the exception will only be raised. This feautre is useful for unit tests.
     *
     * @param message The message to log
     */
    public SimulatorException(String message, boolean runRoutine) {
        LOGGER.error(message);
        if (trapController != null)
            trapController.setFault(getOpcode(), runRoutine);
    }

    public SimulatorException(String message) {
        LOGGER.error(message);
        if (trapController != null)
            trapController.setFault(getOpcode(), false);
    }

    public abstract int getOpcode();

    public static void setTrapController(TrapController trapController) {
        SimulatorException.trapController = trapController;
    }
}
