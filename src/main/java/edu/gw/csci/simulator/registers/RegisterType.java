package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.exceptions.SimulatorException;

/**
 * This framework defines the properties for all registers.
 *
 * @version 20180916
 */
public enum RegisterType {

    PC("Program Counter", 12, ""),
    CC("Condition Code", 4, ""),
    IR("Instruction Register", 16, ""),
    MAR("Memory Address Register", 16, ""),
    MBR("Memory Buffer Register", 16, ""),
    MFR("Machine Fault Register", 16, ""),
    R0("General Purpose Register", 16, "00"),
    R1("General Purpose Register", 16, "01"),
    R2("General Purpose Register", 16, "10"),
    R3("General Purpose Register", 16, "11"),
    X1("Index Register", 16, "01"),
    X2("Index Register", 16, "10"),
    X3("Index Register", 16, "11");

    private final String description;
    private final int size;
    private final String binarycode;

    RegisterType(String description, int size, String binarycode) {
        this.description = description;
        this.size = size;
        this.binarycode = binarycode;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSize() {
        return this.size;
    }

    public String getBinaryCode() {
        return this.binarycode;
    }

    public static RegisterType getGeneralPurpose(int i){
        switch (i){
            case 0: return R0;
            case 1: return R1;
            case 2: return R2;
            case 3: return R3;
            default: throw new SimulatorException("General purpose register out of bounds");
        }
    }

    public static RegisterType getIndex(int i){
        switch (i){
            case 0: return X1;
            case 1: return X2;
            case 2: return X3;
            default: throw new SimulatorException("General purpose register out of bounds");
        }
    }
}
