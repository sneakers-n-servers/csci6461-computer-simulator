package edu.gw.csci.simulator.exceptions;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;

/**
 * Catch-all for exceptions from the application rather than the simulated computer.
 *
 * @version 20180916
 */
public class SimulatorException extends RuntimeException {

    private static Register machineFaultRegister;

    public SimulatorException(String message) {
        super(message);
    }

    public static void setMachineFaultRegister(Register machineFaultRegister){
        SimulatorException.machineFaultRegister = machineFaultRegister;
    }
}
