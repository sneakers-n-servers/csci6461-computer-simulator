package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.isa.Instructions.*;

import java.util.HashMap;

public class Decoder {

    private HashMap<InstructionType, InstructionFactory> instructions = new HashMap<>();

    public Decoder(){
        instructions.put(InstructionType.HLT, new InstructionFactory<>(HLT.class));
        instructions.put(InstructionType.TRAP, new InstructionFactory<>(TRAP.class));
    }

    public InstructionFactory getInstructionFactory(InstructionType instructionType){
        return instructions.get(instructionType);
    }
}
