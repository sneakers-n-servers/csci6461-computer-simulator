package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.gui.BitDecorator;
import javafx.beans.property.SimpleStringProperty;

public class MemoryChunkDecorator extends BitDecorator<MemoryChunk> {

    public MemoryChunkDecorator(MemoryChunk memoryChunk) {
        super(memoryChunk);
    }

    public SimpleStringProperty getIndex() {
        MemoryChunk memoryChunk = super.getBitType();
        return new SimpleStringProperty(Integer.toString(memoryChunk.getIndex()));
    }
}