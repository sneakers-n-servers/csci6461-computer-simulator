package crap;/*
Project 1 - Processor

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

//IMPORT

import edu.gw.csci.simulator.exceptions.IllegalOpcode;
import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;

import java.util.Arrays;

//BEGIN
public class Processor {
    //CONSTANTS
    private static final int OPCODE_SIZE_INBITS = 6;
    // debug constants
    private static final int MODULE_DEBUG_FLAG = 1;

    //VARIABLES
    //TBD private int memory_size;
    private Memory memory;
    private String[] opcodes;
    public Registers registers;

    //CONSTRUCTORS
    public Processor(Memory memory) {
        if (OPCODE_SIZE_INBITS > 8) {
            System.out.println("TBD too many opcodes; need to raise an exception: " + OPCODE_SIZE_INBITS + ".");
            System.exit(-1);
        }
        opcodes = new String[(int) Math.pow(2, OPCODE_SIZE_INBITS)];
        // populate implemented codes - TBD clean this up maybe with a key-value pair
        opcodes[1] = "LDR";
        opcodes[2] = "STR";
        opcodes[3] = "LDA";
        opcodes[33] = "LDX";
        opcodes[34] = "STX";
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("OpCodes: " + Arrays.toString(opcodes) + ".");
        }
        // check memory
        this.memory = memory;
        //TBD memory_size = memory.getSize();
        // initialize registers
        registers = new Registers();
    }

    //BEGIN
    public void step() {
        //VARIABLES
        int current_opcode = 2;
        long current_tick;
        //BEGIN
        // decode instruction
        try {
            current_opcode = decodeInstructionOpcode(registers.getIR());
        } catch (IllegalOpcode bound_error) {
            current_opcode = -1;
            System.out.println("Memory Fault Register = 0x" + Integer.toHexString(registers.getMFR()) + ".");
            bound_error.printStackTrace();
        }
        // fetch instruction
        registers.setIR((int) registers.getMBR());
        // fetch from memory
        try {
            taskMemoryControlUnit_get(memory);
        } catch (MemoryOutOfBounds bound_error) {
            System.out.println("Memory Fault Register = 0x" + Integer.toHexString(registers.getMFR()) + ".");
            bound_error.printStackTrace();
        }
        // read PC
        registers.setMAR(registers.getPC());
        // advance PC - TBD this will have to get more complicated for a jump
        registers.incrementPC();
        // one clock cycle
        current_tick = registers.tick();
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("\n\nThe time is now " + current_tick + " ticks.");
            System.out.println("PC = 0x" + Integer.toHexString(registers.getPC())
                    + "; MAR = 0x" + Integer.toHexString(registers.getMAR())
                    + "; MBR = 0x" + Integer.toHexString(registers.getMBR())
                    + "; IR = 0x" + Integer.toHexString(registers.getIR()) + ".");
        }
    }

    // decode
    public int decodeInstructionOpcode(int current_instruction) throws IllegalOpcode {
        int error = 0;
        String exception_string = "No exception information.";
        int opcode = 0;
        String opcode_string;
        opcode = current_instruction >>> 10;
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Current IR = 0x" + Integer.toHexString(current_instruction)
                    + "; current opcode = " + opcode + ".");
        }
        opcode_string = opcodes[opcode];
        if (MODULE_DEBUG_FLAG == 1) {
            System.out.println("Current opcode string = " + opcode_string);
        }
        if (opcode_string == null) {
            // create exception message
            exception_string = "Illegal opcode!!!\n"
                    + "IR = " + current_instruction
                    + " (ie, 0x" + Integer.toHexString(current_instruction)
                    + "); opcode = " + opcode
                    + " (ie, 0x" + Integer.toHexString(opcode) + ").";
            // optionally display debug info
            if (MODULE_DEBUG_FLAG == 1) {
                System.out.println(exception_string);
            }
            // tag error flag
            error = 1;
        }
        if (error == 1) {
            // set fault register
            registers.setMFR(registers.FAULT_ILLEGAL_OPCODE);
            throw new IllegalOpcode("\n" + exception_string);
        }
        return opcode;
    }

    // memory control unit
    public void taskMemoryControlUnit_get(Memory memory) throws MemoryOutOfBounds {
        int error = 0;
        String exception_string = "No exception information.";
        int memory_size = memory.getSize();
        int memory_address_value;
        short[] memory_words = new short[1];
        // read intended address from register
        memory_address_value = registers.getMAR();
        if (memory_address_value >= memory_size) {
            // create exception message
            exception_string = "Memory out of bounds!!!\n"
                    + "Requested address = " + memory_address_value
                    + " but max available would be " + (memory_size - 1) + ".";
            // optionally display debug info
            if (MODULE_DEBUG_FLAG == 1) {
                System.out.println(exception_string);
            }
            // tag error flag
            error = 1;
        }
        if (error == 0) {
            // fetch from memory
            memory.getWords(memory_words, memory_address_value, 1);
            // this should need no further error checking
            registers.setMBR(memory_words[0]);
        } else {
            // dummy value
            registers.setMBR((short) 0);
            // set fault register
            registers.setMFR(registers.FAULT_MEMORY_OVER);
            throw new MemoryOutOfBounds("\n" + exception_string);
        }
    }
}

