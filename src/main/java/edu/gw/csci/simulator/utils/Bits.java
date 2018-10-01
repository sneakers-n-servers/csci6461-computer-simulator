package edu.gw.csci.simulator.utils;
import java.util.BitSet;
import java.util.stream.IntStream;

import static java.lang.Math.toIntExact;

    /**
     * Methods to facilitate access of inidividual bits within a word.
     *
     * @version 20180918
     */
public class Bits {

    /**
     * Convert an integer to a BitSet
     *
     * @param value The integer to utils
     * @return The converted bitset
     */
	public static BitSet convert(int value) {
        BitSet bitSet = BitSet.valueOf(new long[]{value});
	    return bitSet;
    }


    /**
     *  Convert the Bitset to int, given that size cannot be over
     *  64 bits, we can safely return the first index. If the BitSet is empty,
     *  return 0
     * @param bits The Bitset to utils
     * @return The converted integer
     */
    public static int convert(BitSet bits) {
        if(bits.isEmpty()){
            return 0;
        }
		long l = bits.toLongArray()[0];
        return toIntExact(l);
    }

    /**
     * Converts a bitset to a binary string representation using little endian.
     * The size of the string returned varies on the size of the BitSet, therefore
     * a bit set with the value of 7, and a max of 4 bits will return "0111".
     *
     * @param bits The BitSet to conver
     * @return The binary string representation
     */
    public static String toBinaryString(BitSet bits, int numberOfBits) {
        if(bits.isEmpty()){
            char[] empties = new char[numberOfBits];
            return new String(empties).replace("\0", "0");
        }
        StringBuilder builder = new StringBuilder(numberOfBits);
        IntStream.range(0, numberOfBits).mapToObj(i -> bits.get(i) ? '1' : '0').forEach(builder::append);
        return builder.reverse().toString();
    }
}
