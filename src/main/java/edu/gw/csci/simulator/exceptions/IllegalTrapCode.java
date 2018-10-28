package edu.gw.csci.simulator.exceptions;

public class IllegalTrapCode extends SimulatorException {

    public IllegalTrapCode(String message) {
        super(message);
    }

    @Override
    int getOpcode() {
        return 2;
    }
}
