package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.Bits;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.BitSet;

public class MemoryChunk implements Bits {

    private ObjectProperty<BitSet> data;
    private final int wordSize, index;

    public MemoryChunk(int wordSize, int index) {
        this.wordSize = wordSize;
        this.index = index;
        this.data = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize() {
        BitSet bitSet = new BitSet(wordSize);
        data.set(bitSet);
    }

    @Override
    public void setData(BitSet bitSet) {
        if (bitSet.length() > getSize()) {
            String mess = String.format(
                    "Binary value %s is larger than maximum %d",
                    BitConversion.toBinaryString(bitSet, bitSet.length()),
                    getSize()
            );
            throw new IllegalValue(mess);
        }
        data.setValue(bitSet);
    }

    @Override
    public int getSize() {
        return wordSize;
    }

    @Override
    public BitSet getData() {
        return data.getValue();
    }

    public ObjectProperty<BitSet> getBitSetProperty() {
        return data;
    }

    public int getIndex() {
        return this.index;
    }
}
