package edu.gw.csci.simulator.utils;

import edu.gw.csci.simulator.exceptions.IllegalOpcode;
import edu.gw.csci.simulator.exceptions.IllegalValue;
import edu.gw.csci.simulator.isa.SetCC;

import java.util.BitSet;
import java.util.stream.IntStream;

import static java.lang.Math.toIntExact;

/**
 * Methods to facilitate access of individual bits within a word.
 *
 * @version 20180918
 */
public class BitConversion {

    /**
     * Convert an integer to a BitSet(16bits)
     * Use complement code to show binary number
     *
     * @param value The integer to utils
     * @return The converted BitSet
     * @throws IllegalValue we have 16 bits word, so the range is [-32768,32767],
     *                      numbers that not in the range will throw IllegalValue Exception
     */
    public static BitSet convert(int value) throws IllegalValue {
        if (value >= 0 && value <= SetCC.MaxValue) {
            //handle positive number
            return BitSet.valueOf(new long[]{value});
        } else if (value >= SetCC.MinValue && value < 0) {
            //handle negative number
            BitSet bits;
            value = SetCC.MaxValue + 1 + value;
            bits = BitSet.valueOf(new long[]{value});
            bits.set(15);
            return bits;
        } else {
            //we have 16 bits word, so the range is from -32768-32767
            String mess = String.format("Value: %d is out of range:[%d,%d]", value, SetCC.MinValue, SetCC.MaxValue);
            throw new IllegalValue(mess);
        }
    }


    /**
     * Convert the BitSet(16bits) to int, given that size cannot be over
     * 64 bits (word size is 16bits), we can safely return the first index.
     * If the BitSet is empty,return 0.
     * When we meet a negative number (bits(15)==1), clear bits(15) first, then treat it as positive number,
     * then minus 2^15 = 32768 to get the true value.
     *
     * @param bits The BitSet to utils
     * @return The converted integer
     */
    public static int convert(BitSet bits) throws IllegalValue {
        BitSet bitSet = (BitSet) bits.clone();
        if (bits.length() > 16) {
            String mess = String.format("Value is out of range:[%d,%d]", SetCC.MinValue, SetCC.MaxValue);
            throw new IllegalValue(mess);
        }

        if (bitSet.isEmpty()) {
            return 0;
        }
        if (bitSet.get(15)) {
            //handle negative number
            bitSet.clear(15);
            if (bitSet.isEmpty()) {
                //value = -32768
                return SetCC.MinValue;
            } else {
                long l = bitSet.toLongArray()[0];
                return toIntExact(l) + SetCC.MinValue;
            }
        } else {
            //handle positive number
            long l = bitSet.toLongArray()[0];
            return toIntExact(l);
        }
    }

    /**
     * Convert an integer to a BitSet(32bits)
     * Use complement code to show binary number
     *
     * @param value The integer to utils
     * @return The converted BitSet
     * @throws IllegalValue we have 32 bits word
     *                      numbers that not in the range will throw IllegalValue Exception
     */
    public static BitSet ExtendConvert(int value) throws IllegalValue {
        if (value >= 0 && value <= SetCC.ExtendMaxValue) {
            //handle positive number
            return BitSet.valueOf(new long[]{value});
        } else if (value >= SetCC.ExtendMinValue && value < 0) {
            //handle negative number
            BitSet bits;
            value = SetCC.ExtendMaxValue + 1 + value;
            bits = BitSet.valueOf(new long[]{value});
            bits.set(31);
            return bits;
        } else {
            //we have 32 bits extend word, so the range is from -2^16-2^16-1
            String mess = String.format("Value: %d is out of range:[%d,%d]", value, SetCC.ExtendMinValue, SetCC.ExtendMaxValue);
            throw new IllegalValue(mess);
        }
    }

    /**
     * Convert the BitSet(32bits) to int, given that size cannot be over
     * 64 bits (word size is 16bits), we can safely return the first index.
     * If the BitSet is empty,return 0.
     * When we meet a negative number (bits(31)==1), clear bits(31) first, then treat it as positive number,
     * then minus 2^16 to get the true value.
     *
     * @param bits The BitSet to utils
     * @return The converted integer
     */
    public static int ExtendConvert(BitSet bits) {
        BitSet bitSet = (BitSet) bits.clone();
        if (bits.length() > 32) {
            String mess = String.format("Value is out of range:[%d,%d]", SetCC.ExtendMinValue, SetCC.ExtendMaxValue);
            throw new IllegalValue(mess);
        }
        if (bitSet.isEmpty()) {
            return 0;
        }
        if (bitSet.get(31)) {
            //handle negative number
            bitSet.clear(31);
            if (bitSet.isEmpty()) {
                return SetCC.ExtendMinValue;
            } else {
                long l = bitSet.toLongArray()[0];
                return toIntExact(l) + SetCC.ExtendMinValue;
            }
        } else {
            //handle positive number
            long l = bitSet.toLongArray()[0];
            return toIntExact(l);
        }
    }

    /**
     * Converts a bitset to a binary string representation using little endian.
     * The size of the string returned varies on the size of the BitSet, therefore
     * a bit set with the value of 7, and a max of 4 bits will return "0111".
     *
     * @param bits The BitSet to convert
     * @return The binary string representation
     */
    public static String toBinaryString(BitSet bits, int numberOfBits) {
        if (bits.isEmpty()) {
            char[] empties = new char[numberOfBits];
            return new String(empties).replace("\0", "0");
        }
        StringBuilder builder = new StringBuilder(numberOfBits);
        IntStream.range(0, numberOfBits).mapToObj(i -> bits.get(i) ? '1' : '0').forEach(builder::append);
        return builder.reverse().toString();
    }

    /**
     * Converts an integer to a binary string, and 0 pads up to the number
     * of bits. For instance, calling toBinaryString(3, 6) will return 000011.
     *
     * @param value        A decimal number
     * @param numberOfBits The bit of binary number
     * @return The binary string representation
     */
    public static String toBinaryString(int value, int numberOfBits) {
        BitSet bits = convert(value);
        return toBinaryString(bits, numberOfBits);
    }

    /**
     * Converts an instruction to BitSet
     *
     * @param binaryString an Instruction
     * @return The binary string representation
     * @throws IllegalOpcode An instruction must be binary.
     */
    public static BitSet convert(String binaryString) throws IllegalValue {
        try {
            Integer.parseInt(binaryString, 2);
        } catch (NumberFormatException e) {
            String mess = String.format("%s is not binary", binaryString);
            throw new IllegalValue(mess);
        }

        BitSet bits = new BitSet(binaryString.length());
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1')
                bits.set(binaryString.length() - i - 1);
        }
        return bits;
    }

    /**
     * Converts a binary String to Integer
     *
     * @param s A binaryString
     * @return The Integer value of this binaryString
     */
    public static int fromBinaryStringToInt(String s) {
        return convert(convert(s));
    }
}
