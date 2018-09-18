package edu.gw.csci.simulator.registers;

import java.util.BitSet;

public class Register {

    private final RegisterType registerType;
    private BitSet data;

    public Register(RegisterType registerType){
        if(registerType.getSize() > 64){
            throw new IllegalArgumentException("Can't instantiate size larger than 64 bits");
        }
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

    public int getSize(){
        return registerType.getSize();
    }

    public String getName(){
        return registerType.toString();
    }

    public String getDescription(){
        return registerType.getDescription();
    }



//    public void setData(long l){
//        String binary = Long.toBinaryString(l);
//        for(char c : binary.toCharArray()){
//            data.se
//        }
//    }
}
