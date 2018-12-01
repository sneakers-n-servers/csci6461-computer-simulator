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
        cpu.StoreValue(25, 3);    // int for float
        cpu.StoreValue(26, 4);    // int for float
        cpu.StoreValue(29, 10);    // vectors length
        cpu.StoreValue(30, 5);    // vector 1 [0]
        cpu.StoreValue(31, 5);    //
        cpu.StoreValue(32, 5);    //
        cpu.StoreValue(33, 5);    //
        cpu.StoreValue(34, 5);    //
        cpu.StoreValue(35, 5);    //
        cpu.StoreValue(36, 5);    //
        cpu.StoreValue(37, 5);    //
        cpu.StoreValue(38, 5);    //
        cpu.StoreValue(39, 5);    //
        cpu.StoreValue(40, 10);    // vector 2 [0]
        cpu.StoreValue(41, 10);    //
        cpu.StoreValue(42, 10);    //
        cpu.StoreValue(43, 10);    //
        cpu.StoreValue(44, 10);    //
        cpu.StoreValue(45, 10);    //
        cpu.StoreValue(46, 10);    //
        cpu.StoreValue(47, 10);    //
        cpu.StoreValue(48, 10);    //
        cpu.StoreValue(49, 10);    //
        cpu.StoreValue(54, 0);    // temp
        cpu.StoreValue(55, 7);    // *(program) - change only on fractional shift
        cpu.StoreValue(56, 25);    // *(ints for float)
        cpu.StoreValue(57, 29);    // *(vectors length)
        cpu.StoreValue(58, 30);    // vector 1 address
        cpu.StoreValue(59, 40);    // vector 2 address
    }
}
