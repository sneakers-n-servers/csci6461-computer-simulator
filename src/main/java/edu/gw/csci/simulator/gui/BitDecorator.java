package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.Bits;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.utils.BitConversion;
import edu.gw.csci.simulator.utils.FloatingPointConvert;
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
    public final static String NULL_STRING = "NULL";

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

    /**
     * Sets the integer value of either memory or registers with trap handling.
     *
     * @param i The integer to set
     */
    public void setIntegerValue(int i) throws IllegalValue {
        BitSet bitSet = BitConversion.convert(i);
        if (bitSet.length() > bitType.getSize()) {
            String mess = String.format("Value: %d is greater than max: %d", i, bitType.getSize());
            throw new IllegalValue(mess);
        }
        bitType.setData(bitSet);
    }

    public void setFloatValue(float f) throws IllegalValue {
        BitSet bitSet = FloatingPointConvert.FloatConvert(f);
        if (bitSet.length() > bitType.getSize()) {
            String mess = String.format("Value: %.5f is greater than max: %d", f, bitType.getSize());
            throw new IllegalValue(mess);
        }
        bitType.setData(bitSet);
    }

    /**
     * Ensures that the value to set is of proper size, and sets the data
     * for either memory or registers.
     *
     * @param data The bitset data to set
     * @throws IllegalValue If the bitset length is higher than what is supported
     */
    public void setIntegerValue(BitSet data) throws IllegalValue {
        if (data.length() > bitType.getSize()) {
            String mess = String.format(
                    "Binary value %s is larger than maximum %d",
                    BitConversion.toBinaryString(data, data.length()),
                    bitType.getSize()
            );
            throw new IllegalValue(mess);
        }
        bitType.setData(data);
    }

    /**
     * Converts value from binary with trap handling, and sets the value
     * to either memory, or the proper register.
     *
     * @param data The binary string
     * @throws IllegalValue If the string is not binary
     */
    public void setBinaryValue(String data) throws IllegalValue {
        if (data.length() > bitType.getSize()) {
            String mess = String.format("Value: %s is greater than max: %d", data, bitType.getSize());
            throw new IllegalValue(mess);
        }
        BitSet bitSet = BitConversion.convert(data);
        bitType.setData(bitSet);
    }

    /**
     * Converts the integer data from string, with proper trap handline
     * and sets either the memory or the registers.
     *
     * @param data The string to convert to integer
     * @throws IllegalValue If the string is not an integer
     */
    public void setIntegerValue(String data) throws IllegalValue {
        int value;
        try {
            value = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            String mess = String.format("%s is not a valid integer", data);
            throw new IllegalValue(mess);
        }
        setIntegerValue(value);
    }

    public void setFloatValue(String data){
        float value;
        try {
            value = Float.parseFloat(data);
            System.out.println(String.format("Received value: %.2f", value));
        } catch (NumberFormatException e) {
            String mess = String.format("%s is not a valid integer", data);
            throw new IllegalValue(mess);
        }
        setFloatValue(value);
    }

    /**
     * Converts either registers or memory to the proper binary representation.
     * The value will be zero padded depending on size.
     *
     * @return The converted string
     */
    public String toBinaryString() {
        BitSet data = bitType.getData();
        return BitConversion.toBinaryString(data, bitType.getSize());
    }

    /**
     * Converts either registers or memory to the proper integer representation.
     *
     * @return The converted integer
     */
    public int toInt() {
        BitSet data = bitType.getData();
        return BitConversion.convert(data);
    }

    /**
     * Retrieves the contents of either registers or memory
     *
     * @return The raw {@link BitSet data}
     */
    public BitSet getData() {
        return bitType.getData();
    }
}
