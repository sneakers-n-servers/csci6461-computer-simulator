package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.gui.BitDecorator;
import edu.gw.csci.simulator.utils.BitConversion;
import javafx.beans.property.SimpleStringProperty;

import java.util.BitSet;

public class MemoryChunkDecorator extends BitDecorator<MemoryChunk> {

    public MemoryChunkDecorator(MemoryChunk memoryChunk) {
        super(memoryChunk);
    }

    public SimpleStringProperty getIndex() {
        MemoryChunk memoryChunk = super.getBitType();
        return new SimpleStringProperty(Integer.toString(memoryChunk.getIndex()));
    }
}