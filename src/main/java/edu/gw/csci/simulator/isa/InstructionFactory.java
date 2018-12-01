package edu.gw.csci.simulator.isa;

import java.util.function.Supplier;

public class InstructionFactory<I extends Instruction> {

    private Supplier<I> instruction;

    public InstructionFactory(Supplier<I> instruction) {
        this.instruction = instruction;
    }

    public I create(InstructionType instructionType, String data) {
        I i = instruction.get();
        i.setData(data);
        i.setInstructionType(instructionType);
        return i;
    }
}
