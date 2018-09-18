package edu.gw.csci.simulator.registers;

import java.util.BitSet;

public class Register {

    private final RegisterType registerType;
    private BitSet data;

    public Register(RegisterType registerType){
        this.registerType = registerType;
    }

    public void initialize(){
        data = new BitSet(registerType.getSize());
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public BitSet getData() {
        return data;
    }
}
