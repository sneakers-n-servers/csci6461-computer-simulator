package edu.gw.csci.simulator;
import java.util.BitSet;

public class Bits {

	public static BitSet convert(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static int convert(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1 << i) : 0L;
        }
        return value;
    }
    
    public static String convertToString(BitSet bits, int length) {
    	String S = "";
    	for (int i =0; i < length; i++) {
    		if(bits.get(i)) {
    		S+="1";
    		}
    		else {
    			S+="0";
    		}
    	}
    	return new StringBuffer(S).reverse().toString();
    }
}
