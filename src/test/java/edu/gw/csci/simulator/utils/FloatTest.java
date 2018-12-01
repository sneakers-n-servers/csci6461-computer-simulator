package edu.gw.csci.simulator.utils;

import edu.gw.csci.simulator.cpu.PipeLine;
import org.junit.Test;
import org.junit.Assert;

public class FloatTest {

    @Test
    public void test1(){
        float f = 3.25f;
        //float f = Float.NaN;
        //float f = Float.POSITIVE_INFINITY;
        System.out.println(FloatingPointConvert.FloatConvertToString(f));
        System.out.println(FloatingPointConvert.FloatConvert(FloatingPointConvert.FloatConvert(f)));
        System.out.println((int)f);
    }

    @Test
    public void test2(){
        String s = "0000000001100000";//0.375
        float f = FloatingPointConvert.FloatConvert(s);
        System.out.println(f);
    }

    @Test
    public void test3(){
        String s = "0111111101100000";//NaN
        float f = FloatingPointConvert.FloatConvert(s);
        System.out.println(f);
    }
    @Test
    public void test4(){
        String s = "0111111100000000";//POSITIVE_INFINITY
        float f = FloatingPointConvert.FloatConvert(s);
        System.out.println(f);
    }

    @Test
    public void test5(){
        String s = "1111111100000000";//NEGATIVE_INFINITY
        float f = FloatingPointConvert.FloatConvert(s);
        System.out.println(f);
    }

    @Test
    public void test6(){
        String s = "0100000101100000";//5.5
        float f = FloatingPointConvert.FloatConvert(s);
        System.out.println(f);
    }

    @Test
    public void test7(){
        String s = "1011";//-0.625
        //String s = "0101"; //0.625
        float f = FloatingPointConvert.DecimalRepresentationConvert(s);
        System.out.println(f);
    }

    @Test
    public void test8(){
        String s = "1011";//-0.625
        //String s = "0101"; //0.625
        float f = FloatingPointConvert.DecimalRepresentationConvert(s);
        System.out.println(f);
    }

    @Test
    public void test9(){
        float f = 0.25f;
        String s = FloatingPointConvert.DecimalRepresentationConvertToString(f);
        System.out.println(s);
        System.out.println(FloatingPointConvert.DecimalRepresentationConvert(s));
    }

    @Test
    public void testPipeline(){
        float f1 = 5.5f;
        String s1 = FloatingPointConvert.FloatConvertToString(f1);
        float f2 = -5.25f;
        String s2 = FloatingPointConvert.FloatConvertToString(f2);
        float f3 = 1.0f;
        String s3 = FloatingPointConvert.FloatConvertToString(f3);
        FloatingPointsCalculate[] floatingPointsCalculates = {
                new FloatingPointsCalculate(s1,s2,false),
                new FloatingPointsCalculate(s1,s2,true),
                new FloatingPointsCalculate(s1,s3,false),
                new FloatingPointsCalculate(s2,s3,true),
                new FloatingPointsCalculate(s1,s3,true),
                new FloatingPointsCalculate(s2,s3,false)
        };
        PipeLine pipeLine = new PipeLine(floatingPointsCalculates,10);
        pipeLine.pipeline();
    }
}
