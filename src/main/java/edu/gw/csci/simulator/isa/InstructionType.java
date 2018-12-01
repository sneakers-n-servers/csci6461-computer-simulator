package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.exceptions.IllegalOpcode;
import edu.gw.csci.simulator.utils.BitConversion;

import java.util.HashMap;

/**
 * This framework defines the properties for all instructions in the set.
 *
 * @version 20180916
 */
public enum InstructionType {

    //Miscellaneous
    HLT(0),
    TRAP(36),

    //Load Store instructions
    LDR(1),
    STR(2),
    LDA(3),
    LDX(41),
    STX(42),

    //Transfer instructions
    JZ(10),
    JNE(11),
    JCC(12),
    JMA(13),
    JSR(14),
    RFS(15),
    SOB(16),
    JGE(17),

    //Arithmetic and logic instructions
    AMR(4),
    SMR(5),
    AIR(6),
    SIR(7),
    MLT(20),
    DVD(21),
    TRR(22),
    AND(23),
    ORR(24),
    NOT(25),
    SRC(31),
    RRC(32),

    //IO instructions
    IN(61),
    OUT(62),

    //Floating Point Instructions/Vector Operations:
    FADD(33),
    FSUB(34),
    VADD(43),
    VSUB(44),
    CNVRT(37),
    LDFR(50),
    STFR(51);

    private final int opCode;
    private final String binary;

    private static HashMap<String, InstructionType> instructionMap = new HashMap<>();

    static {
        for (InstructionType it : InstructionType.values()) {
            instructionMap.put(it.binary, it);
        }
    }

    InstructionType(int opCode) {
        //the OpCode above is in Octal, so we transfer them to decimal
        String decimal = Integer.valueOf(String.valueOf(opCode), 8).toString();
        int decimalOpCode = Integer.valueOf(decimal);
        this.opCode = decimalOpCode;
        this.binary = toPadded(decimalOpCode);
    }

    private static String toPadded(int opCode) {
        return BitConversion.toBinaryString(opCode, 6);
    }

    public static InstructionType getInstructionType(String binary) throws IllegalOpcode {
        if (!instructionMap.containsKey(binary)) {
            String mess = String.format("Illegal Opcode: %s non supported operation", binary);
            throw new IllegalOpcode(mess);
        } else {
            return instructionMap.get(binary);
        }
    }

    public int getOpCode() {
        return this.opCode;
    }

    public String getBinary() {
        return binary;
    }
}
