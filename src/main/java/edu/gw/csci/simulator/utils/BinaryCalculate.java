package edu.gw.csci.simulator.utils;

import edu.gw.csci.simulator.isa.SetCC;
import edu.gw.csci.simulator.registers.AllRegisters;

import java.util.BitSet;

public class BinaryCalculate {
    private static String add(String a,String b){
        int carry = 0;
        StringBuilder result = new StringBuilder();
        int plus;
        if(a.length()==b.length()) {
            for (int i = a.length()-1; i >= 0; i--) {
                plus= CharToInt(a.charAt(i))+CharToInt(b.charAt(i))+carry;
                if(plus>=2){carry =1;}
                else {carry =0;}
                if(plus == 0 || plus == 2){result.insert(0,"0");}
                else if(plus==1 || plus ==3){result.insert(0,"1");}
            }
            if (carry == 1){
                result.insert(0,"1");
            }
        }
        return result.toString();

    }

    private static String minus(String a,String b){
        int MinusB = -BitConversion.fromBinaryStringToInt(b);
        String NegativeB = BitConversion.toBinaryString(MinusB,b.length());
        return add(a,NegativeB);
    }

    private static int CharToInt(char c){
        if(c=='1'){
            return 1;
        }
        else if(c=='0'){
            return 0;
        }
        return 0;
    }

    public static BitSet BitAdd(BitSet a,BitSet b){
        String add = add(BitConversion.toBinaryString(a,16),
                BitConversion.toBinaryString(b,16));
        add = add.substring(add.length()-16);
        return BitConversion.fromBinaryStringToBitSet(add);
    }

    public static BitSet BitAdd(BitSet a,int b){
        String add = add(BitConversion.toBinaryString(a,16),
                BitConversion.toBinaryString(b,16));
        add = add.substring(add.length()-16);
        return BitConversion.fromBinaryStringToBitSet(add);
    }

    public static BitSet BitMinus(BitSet a,BitSet b){
        String minus = minus(BitConversion.toBinaryString(a,16),
                BitConversion.toBinaryString(b,16));
        minus = minus.substring(minus.length()-16);
        return BitConversion.fromBinaryStringToBitSet(minus);
    }

    public static BitSet BitMinus(BitSet a,int b){
        String minus = minus(BitConversion.toBinaryString(a,16),
                BitConversion.toBinaryString(b,16));
        minus = minus.substring(minus.length()-16);
        return BitConversion.fromBinaryStringToBitSet(minus);
    }

    public static String BinaryLeftShift(String s, AllRegisters registers){
        if(!s.substring(0,1).equals(s.substring(1,2))){
            registers.OVERFLOW();
        }
        s = s.substring(1)+"0";
        return s;
    }

    public static String BinaryLeftShift(String s, int count,AllRegisters registers){
        for (int i = 0; i < count; i++) {
            s = BinaryLeftShift(s,registers);
        }
        return s;
    }

    public static String BinaryLogicRightShift(String s){
        s = "0"+s.substring(0,s.length()-1);
        return s;
    }

    public static String BinaryLogicRightShift(String s, int count){
        for (int i = 0; i < count; i++) {
            s = BinaryLogicRightShift(s);
        }
        return s;
    }

    public static String BinaryArithmeticalRightShift(String s){
        s = s.substring(0,1)+s.substring(0,s.length()-1);
        return s;
    }

    public static String BinaryArithmeticalRightShift(String s, int count){
        for (int i = 0; i < count; i++) {
            s = BinaryArithmeticalRightShift(s);
        }
        return s;
    }

    public static String BinaryLeftRotate(String s){
        s = s.substring(1)+s.substring(0,1);
        return s;
    }

    public static String BinaryLeftRotate(String s, int count){
        for (int i = 0; i < count; i++) {
            s = BinaryLeftRotate(s);
        }
        return s;
    }

    public static String BinaryRightRotate(String s){
        s = s.substring(s.length()-1)+s.substring(0,s.length()-1);
        return s;
    }

    public static String BinaryRightRotate(String s, int count){
        for (int i = 0; i < count; i++) {
            s = BinaryRightRotate(s);
        }
        return s;
    }
}
