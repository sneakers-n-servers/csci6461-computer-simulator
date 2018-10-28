package edu.gw.csci.simulator.exceptions;

public class IllegalValue extends SimulatorException {

    public IllegalValue(String mess){
        super(mess);
    }

    @Override
    int getOpcode() {
        return 3;
    }
}
