package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.cpu.CPU;

public class PreStoreMemory {
    public static void PreStoreMemoryForProgram1(CPU cpu) {
        cpu.StoreValue(8, 64);
        cpu.StoreValue(9, 170);
        cpu.StoreValue(85, 32767);
        //cpu.StoreValue(85,65535);
        cpu.StoreValue(86, 64);
        cpu.StoreValue(87, 84);
    }

    public static void PreStoreMemoryForProgram2(CPU cpu) {
        cpu.StoreValue(12, 512);
        cpu.StoreValue(13, 64);
        cpu.StoreValue(14, 96);
        cpu.StoreValue(15, 1);
        cpu.StoreValue(16, 1);
    }

    /**
     * This loads the necessary values onto memory for the execution of Program 3 for part IV.
     *
     * @version 20181201
     * @param cpu
     */
    public static void PreStoreMemoryForProgram3(CPU cpu) {
        // this currently assumes memory location 7 for the first instruction - see  SetProgram3
        cpu.StoreValue(21, 0);    // temp
        cpu.StoreValue(22, 7);    // *(program) - change only on fractional shift
        cpu.StoreValue(23, 27);    // *(ints for float)
        cpu.StoreValue(24, 38);    // *(vectors length)
        cpu.StoreValue(25, 39);    // vector 1 address
        cpu.StoreValue(26, 49);    // vector 2 address
        cpu.StoreValue(27, 3);    // int for float
        cpu.StoreValue(28, 4);    // int for float
        cpu.StoreValue(38, 10);    // vectors length
        cpu.StoreValue(39, 5);    // vector 1 [0]
        cpu.StoreValue(40, 5);    //
        cpu.StoreValue(41, 5);    //
        cpu.StoreValue(42, 5);    //
        cpu.StoreValue(43, 5);    //
        cpu.StoreValue(44, 5);    //
        cpu.StoreValue(45, 5);    //
        cpu.StoreValue(46, 5);    //
        cpu.StoreValue(47, 5);    //
        cpu.StoreValue(48, 5);    //
        cpu.StoreValue(49, 10);    // vector 2 [0]
        cpu.StoreValue(50, 10);    //
        cpu.StoreValue(51, 10);    //
        cpu.StoreValue(52, 10);    //
        cpu.StoreValue(53, 10);    //
        cpu.StoreValue(54, 10);    //
        cpu.StoreValue(55, 10);    //
        cpu.StoreValue(56, 10);    //
        cpu.StoreValue(57, 10);    //
        cpu.StoreValue(58, 10);    //
    }
}
