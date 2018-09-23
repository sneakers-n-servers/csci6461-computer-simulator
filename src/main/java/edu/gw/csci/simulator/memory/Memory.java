package edu.gw.csci.simulator.memory;

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
    private BitSet[] memory;

    public Memory(int size, int wordSize){
        this.size = size;
        this.wordSize = wordSize;
    }

    public Memory(){
        this.size = DEFAULT_MEMORY_SIZE;
        this.wordSize = DEFAULT_WORD_SIZE;
    }

    public void initialize(){
        this.memory = new BitSet[this.size];
        for(int i =0; i < this.size; i++){
            this.memory[i] = new BitSet(this.wordSize);
        }
    }

    public BitSet get(int index){
        return this.memory[index];
    }

    public void set(int index, BitSet bitSet){
        this.memory[index] = bitSet;
    }

    public int getWordSize(){
        return wordSize;
    }
}
