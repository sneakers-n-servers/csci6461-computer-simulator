package edu.gw.csci.simulator.utils;

import org.junit.Test;

public class BinaryTest {
    @Test
    public void test1(){
        String s = BitConversion.toBinaryString(5,16);
        System.out.println(s);
        //System.out.println(BinaryCalculate.BinaryLeftShift(s));
        //System.out.println(BinaryCalculate.BinaryLogicRightShift(s));
        //System.out.println(BinaryCalculate.BinaryArithmeticalRightShift(s));
        //System.out.println(BinaryCalculate.BinaryLeftRotate(s));
        System.out.println(BinaryCalculate.BinaryRightRotate(s));
    }

    @Test
    public void test2(){
        //String s ="1000000000000110";
        //String s ="1100000000000011";  right shift 1
        //String s ="1110000000000001";  right shift 2

        String s ="1000000000000001";
        //s = BinaryCalculate.BinaryArithmeticalRightShift(s,1);
        s = BinaryCalculate.BinaryLogicRightShift(s,1);
        int a = BitConversion.fromBinaryStringToInt(s);
        System.out.println(a);
    }
    @Test
    public void test3(){
        String a ="111";
        System.out.println(BinaryCalculate.BinaryAddOne(a));
        System.out.println(BinaryCalculate.BinaryMinusOne(a));
    }
}
