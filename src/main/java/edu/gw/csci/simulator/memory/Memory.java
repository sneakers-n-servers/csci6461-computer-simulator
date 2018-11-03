package edu.gw.csci.simulator.memory;

import javafx.scene.control.TableView;

import java.util.BitSet;

/**
 * Simulates the computer's external (volatile) memory.
 *
 * @version 20180918
 */
public class Memory {

    private static final int DEFAULT_MEMORY_SIZE = 2048,
            DEFAULT_WORD_SIZE = 16;

    private final int size, wordSize;
    private MemoryChunk[] memory;

    public Memory(int size, int wordSize) {
        this.size = size;
        this.wordSize = wordSize;
        this.memory = new MemoryChunk[size];
        for (int i = 0; i < this.size; i++) {
            this.memory[i] = new MemoryChunk(wordSize, i);
        }
    }

    public Memory() {
        this.size = DEFAULT_MEMORY_SIZE;
        this.wordSize = DEFAULT_WORD_SIZE;
        this.memory = new MemoryChunk[DEFAULT_MEMORY_SIZE];
        for (int i = 0; i < this.size; i++) {
            this.memory[i] = new MemoryChunk(DEFAULT_WORD_SIZE, i);
        }
    }

    public void initialize() {
        for (MemoryChunk memoryChunk : memory) {
            memoryChunk.initialize();
        }
    }

    public BitSet getChunkData(int index) {
        MemoryChunk chunk = this.memory[index];
        return chunk.getData();
    }

    public MemoryChunk get(int index) {
        return this.memory[index];
    }

    public void set(int index, BitSet bitSet) {
        MemoryChunk memoryChunk = this.memory[index];
        memoryChunk.setData(bitSet);
    }

    public void bindTableView(TableView tableView) {
        for (MemoryChunk memoryChunk : memory) {
            memoryChunk.getBitSetProperty().addListener((observable, oldValue, newValue) -> tableView.refresh());
        }
    }

    public MemoryChunk[] getMemory() {
        return memory;
    }

    public int getSize() {
        return size;
    }

    public int getWordSize() {
        return wordSize;
    }
}
