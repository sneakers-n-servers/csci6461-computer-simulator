package edu.gw.csci.simulator.isa;

public enum InstructionType {
    LDR("000001"),
    STR("000010"),
    LDA("000011"),
    LDX("101001"),
    STX("101010");

    private final String OpCode;

    InstructionType(String OpCode){
        this.OpCode = OpCode;
    }
    public String getOpCode() {
        return this.OpCode;
    }
}
