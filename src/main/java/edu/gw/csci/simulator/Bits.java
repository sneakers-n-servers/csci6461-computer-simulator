package edu.gw.csci.simulator;

import java.util.BitSet;

public abstract class Bits {

    public abstract void initialize();

    public abstract BitSet getData();

    public abstract void setData(BitSet bitSet);

    public abstract int getSize();
}
