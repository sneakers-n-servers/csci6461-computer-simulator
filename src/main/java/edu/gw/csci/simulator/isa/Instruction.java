package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;


public abstract class Instruction {

    private InstructionType instructionType;
    private String data;

    public abstract void execute(AllMemory memory, AllRegisters registers, CPU cpu);

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(InstructionType instructionType) {
        this.instructionType = instructionType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
