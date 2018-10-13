package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.isa.Instructions.*;

import java.util.HashMap;

public class Decoder {

    private HashMap<InstructionType, InstructionFactory> instructions = new HashMap<>();

    public Decoder(){
        instructions.put(InstructionType.HLT, new InstructionFactory<>(HLT::new));
        instructions.put(InstructionType.LDR, new InstructionFactory<>(LDR::new));
        instructions.put(InstructionType.STR, new InstructionFactory<>(STR::new));
        instructions.put(InstructionType.LDX, new InstructionFactory<>(LDX::new));
        instructions.put(InstructionType.STX, new InstructionFactory<>(STX::new));
    }

    public InstructionFactory getInstructionFactory(InstructionType instructionType){
        return instructions.get(instructionType);
    }
}
