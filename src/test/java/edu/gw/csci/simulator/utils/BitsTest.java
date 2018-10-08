package edu.gw.csci.simulator.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

public class BitsTest {

    @Test
    public void testConvertBstoInt() {
        BitSet bs = new BitSet(3);
        bs.set(0, 3, true);
        BitSet outPut = BitConversion.convert(7);
        Assert.assertEquals(bs, outPut);
    }

    @Test
    public void testConvertIntToBs() {
        int myInt = 7;
        BitSet bs = BitConversion.convert(myInt);
        for (int i = 0; i < bs.length(); i++) {
            Assert.assertTrue(bs.get(i));
        }
    }

    @Test
    public void testtoBinaryString1() {
        BitSet bs = new BitSet(7);
        String s = BitConversion.toBinaryString(bs, 7);
        Assert.assertEquals("0000000", s);
    }

    @Test
    public void testBinaryString2() {
        BitSet bs = new BitSet(3);
        bs.set(0, 3, true);
        String s = BitConversion.toBinaryString(bs, 3);
        Assert.assertEquals("111", s);
    }
}