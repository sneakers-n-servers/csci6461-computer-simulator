package edu.gw.csci.simulator.registers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.BitSet;

public class Register {

    private final RegisterType registerType;
    private ObjectProperty<BitSet> data;

    public Register(RegisterType registerType){
        if(registerType.getSize() > 64){
            throw new IllegalArgumentException("Can't instantiate register size larger than 64 bits");
        }
        this.registerType = registerType;
        this.data = new SimpleObjectProperty<>();
    }

    public void initialize(){
        BitSet bitSet = new BitSet(registerType.getSize());
        data.set(bitSet);
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public BitSet getData() {
        return data.get();
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

    public void setData(BitSet data) {
        this.data.setValue(data);
    }

    public ObjectProperty<BitSet> getBitSetProperty(){
        return this.data;
    }
}
