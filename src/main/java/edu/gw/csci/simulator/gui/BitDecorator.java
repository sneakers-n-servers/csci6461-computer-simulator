package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.Bits;
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
}
