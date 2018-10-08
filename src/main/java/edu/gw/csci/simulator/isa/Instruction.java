package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;


public interface Instruction {

    void execute(AllMemory memory, AllRegisters registers);
    void setData(String data);

}
