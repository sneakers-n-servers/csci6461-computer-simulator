package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;

/**
 * All instructions should extend this class in order to execute proper functionality.
 * Setting data and the instruction type is handled by the factory methods.
 */
public abstract class Instruction {

    private InstructionType instructionType;
    private String data;

    /**
     * Execute the instruction logic
     *
     * @param memory Availible memory
     * @param registers Availible registers
     * @param cpu CPU instance
     */
    public abstract void execute(AllMemory memory, AllRegisters registers, CPU cpu);

    /**
     *
     * @return The type of instruction
     */
    public InstructionType getInstructionType() {
        return instructionType;
    }

    /**
     * @param instructionType The type of instruction
     */
    public void setInstructionType(InstructionType instructionType) {
        this.instructionType = instructionType;
    }

    /**
     * @return The instruction data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the instruction data, not including the type, as retrieved
     * from memory.
     * @param data The instruction data
     */
    public void setData(String data) {
        this.data = data;
    }
}
