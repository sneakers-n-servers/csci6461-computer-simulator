package edu.gw.csci.simulator;

import java.util.BitSet;

public interface Bits {

    void initialize();

    BitSet getData();

    void setData(BitSet bitSet);

    int getSize();
}
