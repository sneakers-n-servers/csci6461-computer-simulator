package edu.gw.csci.simulator.utils;

import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FloatingPointConvert {
    public static final int FLOATING_POINT_BIT =16;
    public static final int EXPONENT_BIT =7;
    public static final int MANTISSA_BIT =8;
    public static final int bias = (int)Math.pow(2,EXPONENT_BIT-1)-1;

    public static String FloatConvertToString(float value){
        //32bits Float to 16bits String
        StringBuilder ValueString32 = new StringBuilder(32);
        StringBuilder ValueString16 = new StringBuilder(16);

        int intBits = Float.floatToIntBits(value);
        ValueString32.append(Integer.toBinaryString(intBits));
        while(ValueString32.length()<32){
            ValueString32.insert(0,"0");
        }
        String FloatString = ValueString32.toString();

        ValueString16.append(FloatString.charAt(0));

        if(isAllZero(FloatString.substring(1,9))){
            ValueString16.append("0000000");
        }
        else if(isAllOne(FloatString.substring(1,9))){
            ValueString16.append("1111111");
        }
        else{
        int exponent = Integer.valueOf(FloatString.substring(1,9),2)-127;
        ValueString16.append(BitConversion.toBinaryString(exponent+bias,EXPONENT_BIT));
        }

        ValueString16.append(FloatString.substring(9,17));


        return ValueString16.toString();
    }
    public static BitSet FloatConvert(float value){
        return BitConversion.convert(FloatConvertToString(value));
    }
    public static float FloatConvert(String value){
        //16bits floating point number, 7bits exponent, 8bits mantissa
        String SString = value.substring(0,1);
        int S = Integer.valueOf(SString);
        String exponentString = value.substring(1,1+EXPONENT_BIT);
        int exponent = Integer.valueOf(exponentString,2);
        String mantissaString= value.substring(value.length()-MANTISSA_BIT);

        float mantissa;

        if(isAllZero(exponentString)){
            mantissa = MantissaConvert(mantissaString);
        }
        else if(isAllOne(exponentString)){
            if(isAllZero(mantissaString)){
                if(SString.equals("0")){
                    mantissa = Float.POSITIVE_INFINITY;
                }
                else {
                    mantissa = Float.NEGATIVE_INFINITY;
                }
            }
            else {
                mantissa = Float.NaN;
            }
        }
        else{
            mantissa = 1+MantissaConvert(mantissaString);
            mantissa = (float)Math.pow(-1,S)*mantissa*(float)Math.pow(2,exponent-bias);
        }

        return mantissa;
    }
    public static float FloatConvert(BitSet bits){
        return FloatConvert(BitConversion.toBinaryString(bits,16));
    }

    private static float MantissaConvert(String mantissa){
        float f = 0;
        for (int i = 0; i < mantissa.length(); i++) {
            if(mantissa.charAt(i)=='1'){
                f = f+(float)Math.pow(2,-(i+1));
            }
        }
        return f;
    }

    public static float DecimalRepresentationConvert(BitSet bits){
        String s = BitConversion.toBinaryString(bits,16);
        return DecimalRepresentationConvert(s);
    }
    public static float DecimalRepresentationConvert(String s){
        //fix point Number
        float f;
        if(s.charAt(0)=='0'){
            f= MantissaConvert(s.substring(1));
        }
        else {
            String s2 = BinaryCalculate.BinaryMinusOne(s.substring(1));

            s2 = flip(s2);
            f = -MantissaConvert(s2);
        }
        return f;
    }
    public static String DecimalRepresentationConvertToString(float f){
        if(f<1) {
            return BitConversion.toBinaryString((int)(f*32768),16);
        }
        else {
            return "";
        }
    }
    public static BitSet DecimalRepresentationConvert(float f){
        return BitConversion.convert(DecimalRepresentationConvertToString(f));
    }

    private static boolean isAllZero(String s){
        Pattern pattern = Pattern.compile("0*");
        return pattern.matcher(s).matches();
    }

    private static boolean isAllOne(String s){
        Pattern pattern = Pattern.compile("1*");
        return pattern.matcher(s).matches();
    }

    private static String flip(String s){
        StringBuilder s2 = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='1'){
                s2.append("0");
            }
            else {
                s2.append("1");
            }
        }
        return s2.toString();
    }
}
