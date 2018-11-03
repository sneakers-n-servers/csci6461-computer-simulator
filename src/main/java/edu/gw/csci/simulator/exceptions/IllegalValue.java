package edu.gw.csci.simulator.exceptions;

public class IllegalValue extends SimulatorException {

    public static final int OP_CODE = 3;

    public IllegalValue(String mess){
        super(mess);
    }

    @Override
    public int getOpcode() {
        return OP_CODE;
    }
}
