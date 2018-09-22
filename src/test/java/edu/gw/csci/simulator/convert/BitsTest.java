package edu.gw.csci.simulator.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class BitsTest {

    @Test
    public void testConvertInt(){
        BitSet bs = new BitSet(3);
        bs.set(0, 3, true);
        BitSet outPut = Bits.convert(7);
        Assert.assertEquals(bs, outPut);
    }
}