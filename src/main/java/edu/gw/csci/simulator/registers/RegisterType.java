package edu.gw.csci.simulator.registers;

public enum RegisterType {

    PC("Program Counter", 12),
    CC("Condition Code", 4),
    IR("Instruction Register", 16),
    MAR("Memory Address Register", 16),
    MBR("Memory Buffer Register", 16),
    MFR("Machine Fault Register", 16),
    R0("General Purpose Register 0", 16),
    R1("General Purpose Register 1", 16),
    R2("General Purpose Register 2", 16),
    R3("General Purpose Register 3", 16),
    X1("Index Register 1", 16),
    X2("Index Register 2", 16),
    X3("Index Register 3", 16);

    private final String description;
    private final int size;

    RegisterType(String description, int size){
        this.description = description;
        this.size = size;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSize() {
        return this.size;
    }
}


