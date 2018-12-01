package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.exceptions.IllegalOpcode;
import edu.gw.csci.simulator.isa.instructions.*;
import edu.gw.csci.simulator.utils.BitConversion;

import java.util.BitSet;
import java.util.HashMap;

public class Decoder {

    private HashMap<InstructionType, InstructionFactory> instructions = new HashMap<>();

    public Decoder() {
        //Miscellaneous
        instructions.put(InstructionType.HLT, new InstructionFactory<>(Miscellaneous.HLT::new));
        instructions.put(InstructionType.TRAP, new InstructionFactory<>(Miscellaneous.TRAP::new));
        //LoadStore
        instructions.put(InstructionType.LDR, new InstructionFactory<>(LoadStore.LDR::new));
        instructions.put(InstructionType.LDA, new InstructionFactory<>(LoadStore.LDA::new));
        instructions.put(InstructionType.STR, new InstructionFactory<>(LoadStore.STR::new));
        instructions.put(InstructionType.LDX, new InstructionFactory<>(LoadStore.LDX::new));
        instructions.put(InstructionType.STX, new InstructionFactory<>(LoadStore.STX::new));
        //Transfer
        instructions.put(InstructionType.JZ, new InstructionFactory<>(Transfer.JZ::new));
        instructions.put(InstructionType.JNE, new InstructionFactory<>(Transfer.JNE::new));
        instructions.put(InstructionType.JCC, new InstructionFactory<>(Transfer.JCC::new));
        instructions.put(InstructionType.JMA, new InstructionFactory<>(Transfer.JMA::new));
        instructions.put(InstructionType.JSR, new InstructionFactory<>(Transfer.JSR::new));
        instructions.put(InstructionType.RFS, new InstructionFactory<>(Transfer.RFS::new));
        instructions.put(InstructionType.SOB, new InstructionFactory<>(Transfer.SOB::new));
        instructions.put(InstructionType.JGE, new InstructionFactory<>(Transfer.JGE::new));
        //ArithmeticLogic
        instructions.put(InstructionType.AMR, new InstructionFactory<>(ArithmeticLogic.AMR::new));
        instructions.put(InstructionType.SMR, new InstructionFactory<>(ArithmeticLogic.SMR::new));
        instructions.put(InstructionType.AIR, new InstructionFactory<>(ArithmeticLogic.AIR::new));
        instructions.put(InstructionType.SIR, new InstructionFactory<>(ArithmeticLogic.SIR::new));
        instructions.put(InstructionType.MLT, new InstructionFactory<>(ArithmeticLogic.MLT::new));
        instructions.put(InstructionType.DVD, new InstructionFactory<>(ArithmeticLogic.DVD::new));
        instructions.put(InstructionType.TRR, new InstructionFactory<>(ArithmeticLogic.TRR::new));
        instructions.put(InstructionType.AND, new InstructionFactory<>(ArithmeticLogic.AND::new));
        instructions.put(InstructionType.ORR, new InstructionFactory<>(ArithmeticLogic.ORR::new));
        instructions.put(InstructionType.NOT, new InstructionFactory<>(ArithmeticLogic.NOT::new));
        //ShiftRotate
        instructions.put(InstructionType.SRC, new InstructionFactory<>(ShiftRotate.SRC::new));
        instructions.put(InstructionType.RRC, new InstructionFactory<>(ShiftRotate.RRC::new));

        //IO
        instructions.put(InstructionType.IN, new InstructionFactory<>(IO.IN::new));
        instructions.put(InstructionType.OUT, new InstructionFactory<>(IO.OUT::new));

        //FloatingPointVector
        instructions.put(InstructionType.FADD, new InstructionFactory<>(FloatingPointVector.FADD::new));
        instructions.put(InstructionType.FSUB, new InstructionFactory<>(FloatingPointVector.FSUB::new));
        instructions.put(InstructionType.VADD, new InstructionFactory<>(FloatingPointVector.VADD::new));
        instructions.put(InstructionType.VSUB, new InstructionFactory<>(FloatingPointVector.VSUB::new));
        instructions.put(InstructionType.CNVRT, new InstructionFactory<>(FloatingPointVector.CNVRT::new));
        instructions.put(InstructionType.LDFR, new InstructionFactory<>(FloatingPointVector.LDFR::new));
        instructions.put(InstructionType.STFR, new InstructionFactory<>(FloatingPointVector.STFR::new));
    }

    /**
     * This method receives a BitSet of data, determines the instruction type, and
     * returns a new Instance of the specific instruction to be executed.
     *
     * @param data The BitSet from Memory, deemed to be an instruction
     * @return A new instance of the Instruction
     */
    public Instruction getInstruction(BitSet data) throws IllegalOpcode {
        String binary = BitConversion.toBinaryString(data, 16);
        String typeString = binary.substring(0, 6);
        String instructionData = binary.substring(6);
        InstructionType instructionType = InstructionType.getInstructionType(typeString);
        return instructions.get(instructionType).create(instructionType, instructionData);
    }
}