package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.Bits;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.beans.property.SimpleStringProperty;

import java.util.BitSet;

/**
 * This class handles the transformation of a {@link Bits} object to be displayed on the GUI.
 * The goal is to provide some level of abstraction between the GUI and the actual running of the simulator.
 * Both the {@link edu.gw.csci.simulator.registers.Register} and the {@link edu.gw.csci.simulator.memory.MemoryChunk}
 * are set as a decorated object within this class, and the GUI utilizes the transformational methods to display the
 * proper value
 *
 * @param <B> The object class to be decorated.
 */
public class BitDecorator<B extends Bits> {

    private B bitType;
    private final static String NULL_STRING = "NULL";

    public BitDecorator(B bitType) {
        this.bitType = bitType;
    }

    /**
     * This method converts an instance of {@link Bits} to it's binary string form, wrapped in a
     * {@link SimpleStringProperty} for display in the GUI.
     *
     * @return The Binary String
     */
    public SimpleStringProperty toBinaryObservableString() {
        BitSet bitSet = bitType.getData();
        String str = (bitSet == null) ? NULL_STRING : BitConversion.toBinaryString(bitSet, bitType.getSize());
        return new SimpleStringProperty(str);
    }

    /**
     * This method converts an instance of {@link Bits} to it's long form, wrapped in a
     * {@link SimpleStringProperty} for display in the GUI. We include this for convenience
     * for evaluating the values held in binary.
     *
     * @return The Long String
     */
    public SimpleStringProperty toLongObservableString() {
        BitSet bitSet = bitType.getData();
        String str = (bitSet == null) ? NULL_STRING : Integer.toString(BitConversion.convert(bitSet));
        return new SimpleStringProperty(str);
    }

    /**
     * Getter of the decorated object.
     *
     * @return The decorated object.
     */
    public B getBitType() {
        return bitType;
    }

    public void setValue(int i) {
        String binary = Integer.toBinaryString(i);
        if(binary.length() > bitType.getSize()){
            String mess = String.format("Value: %d is greater than max: %d", i, bitType.getSize());
            throw new IllegalValue(mess);
        }
        BitSet bitSet = BitConversion.convert(i);
        bitType.setData(bitSet);
    }

    /**
     *
     * @param data
     */
    public void setBinaryValue(String data) throws IllegalValue{
        if(data.length() > bitType.getSize()){
            String mess = String.format("Value: %s is greater than max: %d", data, bitType.getSize());
            throw new IllegalValue(mess);
        }
        BitSet bitSet = BitConversion.convert(data);
        bitType.setData(bitSet);
    }

    public void setIntegerValue(String data) throws IllegalValue{
        Integer value;
        try{
            value = Integer.parseInt(data);
        }catch (NumberFormatException e){
            String mess = String.format("%s is not a valid integer", data);
            throw new IllegalValue(mess);
        }
        setValue(value);
    }

    /**
     *
     * @return
     */
    public String toBinaryString() {
        BitSet data = bitType.getData();
        return BitConversion.toBinaryString(data, bitType.getSize());
    }

    /**
     *
     * @return
     */
    public int toInt() {
        BitSet data = bitType.getData();
        return BitConversion.convert(data);
    }
}
