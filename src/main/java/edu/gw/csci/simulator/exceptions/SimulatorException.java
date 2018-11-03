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

    public SimulatorException(String message) {
        LOGGER.error(message);
        if (trapController != null)
            trapController.setFault(getOpcode());
    }

    public abstract int getOpcode();

    public static void setTrapController(TrapController trapController){
        SimulatorException.trapController = trapController;
    }
}
