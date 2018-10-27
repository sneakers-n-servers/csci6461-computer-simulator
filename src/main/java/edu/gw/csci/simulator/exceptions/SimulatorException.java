package edu.gw.csci.simulator.exceptions;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Catch-all for exceptions from the application rather than the simulated computer.
 *
 * @version 20180916
 */
public abstract class SimulatorException extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger(SimulatorException.class);

    private static RegisterDecorator faultRegisterDecorator;

    public SimulatorException(String message) {
        LOGGER.error(message);
        setFault();
    }

    public static void setMachineFaultRegister(Register machineFaultRegister){
        SimulatorException.faultRegisterDecorator = new RegisterDecorator(machineFaultRegister);
    }

    abstract int getOpcode();

    public void setFault(){
        int opCode = this.getOpcode();
        System.out.println(opCode + "!!!!!!!!!");
        SimulatorException.faultRegisterDecorator.setValue(opCode);
    }
}
