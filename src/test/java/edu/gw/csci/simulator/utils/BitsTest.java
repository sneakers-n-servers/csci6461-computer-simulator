package edu.gw.csci.simulator.utils;

import edu.gw.csci.simulator.exceptions.IllegalOpcode;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.isa.SetCC;
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
        System.out.println(bs);
    }

    @Test
    public void testtoBinaryString1() {
        BitSet bs = new BitSet(7);
        String s = BitConversion.toBinaryString(bs, 7);
        Assert.assertEquals("0000000", s);
        System.out.println(s);
    }

    @Test
    public void testBinaryString2() {
        BitSet bs = new BitSet(3);
        bs.set(0, 3, true);
        String s = BitConversion.toBinaryString(bs, 3);
        Assert.assertEquals("111", s);
    }

    @Test
    public void testBinaryString3() {
        int val = 7;
        String s = BitConversion.toBinaryString(val, 6);
        Assert.assertEquals("000111", s);
    }

    @Test
    public void testConvertString(){
        String binary = "111";
        BitSet bitSet = BitConversion.convert(binary);
        for(int i = 0; i < 3; i++){
            Assert.assertTrue(bitSet.get(i));
        }
        int reconvert = BitConversion.convert(bitSet);
        Assert.assertEquals(7, reconvert);
    }

//    @Test (expected = IllegalValue.class)
//    public void testConvert1(){
//        int a = -32769;
//        //System.out.println(a>=0&&a<=SetCC.MaxValue);
//        //System.out.println(a>= SetCC.MinValue&&a<0);
//        BitSet bits = BitConversion.convert(a);
//    }

    @Test
    public void testConvert2(){
        int a = -32768;
        BitSet bits = BitConversion.convert(a);
        System.out.println(bits);
        int b = BitConversion.convert(bits);
        System.out.println(b);
    }

    @Test
    public void testConvert3(){
        //String s = "0000110000011001";
        String s = "1000010001011110";
        BitSet bits1 = BitConversion.convert(s);
        int a = BitConversion.fromBinaryStringToInt(s);
        //int b = BitConversion.convert()
        BitSet bits2 = BitConversion.convert(s);
        String s2 = BitConversion.toBinaryString(bits2,16);
        String s3 = BitConversion.toBinaryString(a,16);
        System.out.println(bits1);
        System.out.println(bits2);
        System.out.println(a);
        System.out.println(s2);
        System.out.println(s3);

    }


}