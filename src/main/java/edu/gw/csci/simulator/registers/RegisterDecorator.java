package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.App;
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

    /***
     * Convert the Bitset to long, given that size cannot be over
     * 64 bits, we can safely return the first index
     *
     * @return value of the register expressed as a long
     */
    public long toLong(){
        BitSet data = register.getData();
        if(data.isEmpty()){
            return 0L;
        }
        long[] longArray = data.toLongArray();
        return longArray[0];
    }

    public int toInt(){
        return toIntExact(toLong());
    }

    public String toBinaryString(){
        BitSet data = register.getData();
        int numberOfBits = register.getSize();
        if(data.isEmpty()){
            char[] empties = new char[numberOfBits];
            return new String(empties).replace("\0", "0");
        }
        StringBuilder builder = new StringBuilder(numberOfBits);
        IntStream.range(0, numberOfBits).mapToObj(i -> data.get(i) ? '1' : '0').forEach(builder::append);
        return builder.reverse().toString();
    }

    public SimpleStringProperty toBinaryObservableString(){
        String str = (register.getData() == null) ? NULL_STRING : toBinaryString();
        return new SimpleStringProperty(str);
    }

    public SimpleStringProperty toLongObservableString(){
        String str = (register.getData() == null) ? NULL_STRING : Long.toString(toLong());
        return new SimpleStringProperty(str);
    }

    public SimpleStringProperty getRegisterName(){
        return new SimpleStringProperty(register.getRegisterType().toString());
    }

    public void setRegister(int i){
        BitSet bitSet = convert(i);
        this.register.setData(bitSet);
    }

    public static BitSet convert(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bits.set(index);
            }
            index++;
            value = value >>> 1;
        }
        System.out.println(bits);
        return bits;
    }

}
