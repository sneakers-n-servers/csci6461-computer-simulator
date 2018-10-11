package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.utils.BitConversion;

import java.util.HashMap;

/**
 * This framework defines the properties for all instructions in the set.
 *
 * @version 20180916
 */
public enum InstructionType {
    AIR(6),
    AMR(4)//Arithmetic and logic instructions
    ,

    AND(23),
    CHK(63),
    DVD(21),
    HLT(0)//Miscellaneous
    ,
    IN(61)//IO instructions
    ,

    JCC(12),
    JGE(17),
    JMA(13),
    JNE(11),
    JSR(14),
    JZ(10)//Transfer instructions
    ,
    LDA(3),
    LDR(1)//Load Store instructions
    ,

    LDX(41),
    MLT(20),
    NOT(25),
    ORR(24),
    OUT(62),
    RFS(15),
    RRC(32),
    SIR(7),
    SMR(5),
    SOB(16),
    SRC(31),
    STR(2),

    STX(42),
    TRAP(36),
    TRR(22);

    private final int opCode;
    private final String binary;

    private static HashMap<String, InstructionType> instructionMap = new HashMap<>();

    static {
        for (InstructionType it : InstructionType.values()) {
            instructionMap.put(it.binary, it);
        }
    }

    InstructionType(int opCode) {
        this.opCode = opCode;
        this.binary = toPadded(opCode);
    }

    private static String toPadded(int opCode) {
        return BitConversion.toBinaryString(opCode,6);
    }

    public static InstructionType getInstructionType(String binary) {
        return instructionMap.get(binary);
    }

    public int getOpCode() {
        return this.opCode;
    }

    public String getBinary() {
        return binary;
    }
}
