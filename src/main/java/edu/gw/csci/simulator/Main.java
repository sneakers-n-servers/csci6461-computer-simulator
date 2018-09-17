package edu.gw.csci.simulator;/*
Project 1 - Main entry

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

import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;

import java.util.Arrays;


//BEGIN
public class Main {
    //CONSTANTS
    private static final int MEMORY_SIZE = 2048;
    // debug constants
    private static final int MODULE_DEBUG_FLAG = 1;
    private static final int CLOCK_STEPS = 5;
    //VARIABLES
    private Memory memory;

    //BEGIN
    public static void main(String[] args) {
        // create memory
        Memory memory = new Memory(MEMORY_SIZE);
        // prepare processor
        Processor pdp8 = new Processor(memory);

        // write something valid-ish into memory
        int number_of_data = memory.getSize();
        short fake_ldr = (short) (1 << 10);
        short[] ldr_repeated = new short[number_of_data];
        for (int i = 0; i < 9; i++) {
            ldr_repeated[i] = (short) 0;
        }
        for (int i = 9; i < number_of_data; i++) {
            ldr_repeated[i] = fake_ldr;
        }
        try {
            memory.setWords(ldr_repeated, 0);
        } catch (MemoryOutOfBounds bound_error) {
            bound_error.printStackTrace();
        }

        // demo
        pdp8.registers.setPC(8);
        for (int i = 0; i < CLOCK_STEPS; i++) {
            System.out.println("PC = " + pdp8.registers.getPC());
            pdp8.step();
        }
    }
}

