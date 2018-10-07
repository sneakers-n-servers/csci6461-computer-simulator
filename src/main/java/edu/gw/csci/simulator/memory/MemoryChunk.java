package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.Bits;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.BitSet;

public class MemoryChunk extends Bits {

    private ObjectProperty<BitSet> data;
    private final int wordSize, index;

    public MemoryChunk(int wordsize, int index){
        this.wordSize = wordsize;
        this.index = index;
        this.data = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(){
        BitSet bitSet = new BitSet(wordSize);
        data.set(bitSet);
    }

    @Override
    public void setData(BitSet bitSet){
        data.setValue(bitSet);
    }

    @Override
    public int getSize() {
        return wordSize;
    }

    @Override
    public BitSet getData(){
        return data.getValue();
    }

    public ObjectProperty<BitSet> getBitSetProperty(){
        return data;
    }

    public int getIndex(){
        return this.index;
    }
}
