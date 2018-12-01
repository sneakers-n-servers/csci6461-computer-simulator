package edu.gw.csci.simulator;

import java.util.BitSet;

/**
 * Interface for {@link edu.gw.csci.simulator.registers.Register registers} and {@link edu.gw.csci.simulator.memory.MemoryChunk memory}
 */
public interface Bits {

    /**
     * Initialize the {@link BitSet} data from null to a value of
     * proper length
     */
    void initialize();

    /**
     * Gets the {@link BitSet}
     * @return The bitset
     */
    BitSet getData();

    /**
     * @param bitSet The data to set
     */
    void setData(BitSet bitSet);

    /**
     * @return The maxiumum size of the BitSet
     */
    int getSize();
}
