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
}
