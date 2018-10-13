package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.isa.Instructions.*;
import edu.gw.csci.simulator.utils.BitConversion;

import java.util.BitSet;
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

    /**
     * This method receives a BitSet of data, determines the instruction type, and
     * returns a new Instance of the specific instruction to be executed.
     *
     * @param data The BitSet from Memory, deemed to be an instruction
     * @return A new instance of the Instruction
     */
    public Instruction getInstruction(BitSet data){
        String binary = BitConversion.toBinaryString(data, 16);
        String typeString = binary.substring(0, 6);
        String instructionData = binary.substring(6);
        InstructionType instructionType = InstructionType.getInstructionType(typeString);
        return instructions.get(instructionType).create(instructionData);
    }
}
