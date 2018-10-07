package edu.gw.csci.simulator.exceptions;

/**
 * Catch-all for exceptions from the application rather than the simulated computer.
 *
 * @version 20180916
 */
public class SimulatorException extends RuntimeException {

    public SimulatorException(String message) {
        super(message);
    }
}
