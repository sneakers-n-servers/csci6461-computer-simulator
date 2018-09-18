/*
Project 1 - CPU Registers

%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
%%%%%    Revision:    20180916
%%%%%    %%%%%%%%%%    %%%%%%%%%%    %%%%%
*/

/*
%%%%%%%%%%
REVISION HISTORY

%%%     20180916
        Modified:
    -

%%%     20180916
        Author: Group 9
        Project:    CSCI_6461_F18_Project1
    -Initial release

%%%%%%%%%%
*/
package crap;


//IMPORT

import edu.gw.csci.simulator.exceptions.*;

//BEGIN
public class Registers {
    //CONSTANTS
    public static final int FAULT_MEMORY_OVER = 0x8;
    public static final int FAULT_ILLEGAL_OPCODE = 0x4;
    private static final int DEFAULT_MEMORY_WORD_SIZE_INBITS = 16;
    private static final int DEFAULT_GPR_WORDS = 4;
    private static final int DEFAULT_IX_WORDS = 4;
    // debug constants
    private static final int MODULE_DEBUG_FLAG = 1;

    //VARIABLES
    // tick clock
    private long clock_value;
    // Logic Result Register
    private int LRR;
    // General Purpose Registers (16 bits)
    private short[] R;
    // Index Registers (IX) (16 bits)
    private int[] X;
    // Program Counter (12 bits)
    private int PC;
    // Condition Code (4 bits)
    private int CC;
    // Instruction Register (16 bits)
    private int IR;
    // Memory Address Register (16 bits)
    private int MAR;
    // Memory Buffer Register (16 bits)
    private short MBR;
    // Machine Status Register (16 bits)
    private int MSR;
    // Machine Fault Register (4 bits)
    private int MFR;

    //CONSTRUCTORS
    public Registers() {
        clock_value = 0;
        LRR = 0;
        R = new short[DEFAULT_GPR_WORDS];
        X = new int[DEFAULT_IX_WORDS];
        PC = 0;
        CC = 0;
        IR = 0;
        MAR = 0;
        MBR = 0;
        MSR = 0;
        MFR = 0;
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Registers cleared.");
        }
    }

    //ACCESS
    // clock
    public long tick() {
        clock_value++;
        return clock_value;
    }

    // GPR
    // get values from the GPR
    public void getR(short[] destination, int first_index, int number_of_words) throws MemoryOutOfBounds {
        //VARIABLES
        int error = 0;
        String exception_string = "No exception information.";
        // check for index errors
        if ((first_index < 0) || (number_of_words < 0) || (first_index + number_of_words > R.length)) {
            // create exception message
            exception_string = "Memory out of bounds!!!\n"
                    + "(First index = " + first_index
                    + ") + (Number of words = " + number_of_words + ") assumes " + (first_index + number_of_words)
                    + " word(s) but max available would be " + (R.length - first_index)
                    + " word(s) from that index.";
            // optionally display debug info
            if (MODULE_DEBUG_FLAG == 1) {
                System.out.println(exception_string);
            }
            // tag error flag
            error = 1;
        }
        // handle the error case in which at least the first index is valid
        if (error == 1) {
            if ((first_index >= 0) && (first_index < R.length)) {
                // modify the upper bound
                number_of_words = R.length - first_index;
            } else {
                // even first index is bad so just quit
                throw new MemoryOutOfBounds("\n" + exception_string);
            }
        }
        // finally... now actually get the data
        try {
            System.arraycopy(R, first_index, destination, 0, number_of_words);
        } catch (ArrayIndexOutOfBoundsException bound_error) {
            // in case there was a weird out of bounds
            throw new MemoryOutOfBounds("\n\nSort of MemoryOutOfBounds but we missed it\n\n.");
        }
        // don't forget to throw exception if only some data were copied
        if (error == 1) {
            throw new MemoryOutOfBounds("\n" + exception_string
                    + "\nCopied " + number_of_words + " word(s) before throw.");
        }
    }

    // put values into the GPR
    public void setR(short[] source, int first_index) throws MemoryOutOfBounds {
        int number_of_words = source.length;
        System.arraycopy(source, 0, R, first_index, number_of_words);
    }

    // IX
    public int getX(int first_index) {
        return X[first_index];
    }

    public void setX(int source, int first_index) {
        X[first_index] = source;
    }

    // MFR
    public void setMFR(int fault_code) {
        // mask the code to 4 bits
        fault_code &= 0x000F;
        MFR |= fault_code;
    }

    public int getMFR() {
        return MFR;
    }

    // MAR
    public void setMAR(int address_to_fetch) {
        MAR = address_to_fetch;
    }

    public int getMAR() {
        return MAR;
    }

    // MBR
    public void setMBR(short word) {
        MBR = word;
    }

    public short getMBR() {
        return MBR;
    }

    public void setPC(int address_of_next_instruction) {
        //TBD might need some bound checking here
        PC = address_of_next_instruction;
    }

    public void incrementPC() {
        //TBD implement a modulo here and probably bound-check
        PC += 1;
    }

    public int getPC() {
        return PC;
    }

    public void setIR(int current_instruction) {
        IR = current_instruction;
    }

    public int getIR() {
        return IR;
    }
}

