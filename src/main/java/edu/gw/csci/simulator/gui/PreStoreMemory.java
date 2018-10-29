package edu.gw.csci.simulator.gui;

import edu.gw.csci.simulator.cpu.CPU;

public class PreStoreMemory {
    public static void PreStoreMemoryForProgram1(CPU cpu){
        cpu.StoreValue(64,8);
        cpu.StoreValue(170,9);
        cpu.StoreValue(65535,85);
        cpu.StoreValue(64,86);
        cpu.StoreValue(84,87);
        //cpu.StoreValue(6,0);
    }
}
