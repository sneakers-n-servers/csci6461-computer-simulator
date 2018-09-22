package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.App;
import edu.gw.csci.simulator.convert.Bits;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;
import java.util.stream.IntStream;

import static java.lang.Math.toIntExact;

public class RegisterDecorator {

    private final Register register;
    private final static String NULL_STRING = "NULL";

    private static final Logger logger = LogManager.getLogger(RegisterDecorator.class);

    public RegisterDecorator(Register register){
        this.register = register;
    }

    public int toInt(){
        BitSet data = register.getData();
        return Bits.convert(data);
    }

    public String toBinaryString(){
        BitSet data = register.getData();
        return Bits.toBinaryString(data, register.getSize());
    }

    public SimpleStringProperty toBinaryObservableString(){
        BitSet bitSet = register.getData();
        String str = (bitSet == null) ? NULL_STRING : Bits.toBinaryString(bitSet, register.getSize());
        return new SimpleStringProperty(str);
    }

    public SimpleStringProperty toLongObservableString(){
        BitSet bitSet = register.getData();
        String str = (bitSet == null) ? NULL_STRING : Integer.toString(Bits.convert(bitSet));
        return new SimpleStringProperty(str);
    }

    public SimpleStringProperty getRegisterName(){
        return new SimpleStringProperty(register.getRegisterType().toString());
    }

    public void setRegister(int i){
        BitSet bitSet = Bits.convert(i);
        this.register.setData(bitSet);
    }
}
