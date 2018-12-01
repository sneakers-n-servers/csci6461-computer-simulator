package edu.gw.csci.simulator.registers;

import edu.gw.csci.simulator.exceptions.IllegalRegisterAccess;
import edu.gw.csci.simulator.exceptions.IllegalValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This framework defines the properties for all registers.
 *
 * @version 20180916
 */
public enum RegisterType {

    PC("Program Counter", 12),
    CC("Condition Code", 4),
    IR("Instruction Register", 16),
    MAR("Memory Address Register", 16),
    MBR("Memory Buffer Register", 16),
    MFR("Machine Fault Register", 16),
    R0("General Purpose Register", 16),
    R1("General Purpose Register", 16),
    R2("General Purpose Register", 16),
    R3("General Purpose Register", 16),
    X1("Index Register", 16),
    X2("Index Register", 16),
    X3("Index Register", 16),
    FR0("Floating Point Register",16),
    FR1("Floating Point Register",16);

    private final String description;
    private final int size;

    RegisterType(String description, int size) {
        this.description = description;
        this.size = size;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSize() {
        return this.size;
    }

    private static final Set<RegisterType> floatingPointTypes = new HashSet<>(Arrays.asList(FR0, FR1));
    public static Set<RegisterType> getFloatingPointTypes(){
        return floatingPointTypes;
    }

    public static RegisterType getGeneralPurpose(String index) throws IllegalRegisterAccess, IllegalValue {
        int i = 0;
        try {
            i = Integer.parseInt(index, 2);
        } catch (NumberFormatException e) {
            String mess = String.format("General purpose register index: %s is not binary", i);
            throw new IllegalValue(mess);
        }
        return getGeneralPurpose(i);
    }

    public static RegisterType getGeneralPurpose(int index) throws IllegalRegisterAccess {
        switch (index) {
            case 0:
                return R0;
            case 1:
                return R1;
            case 2:
                return R2;
            case 3:
                return R3;
            default:
                String mess = String.format("General purpose register index: %d out of bounds.", index);
                throw new IllegalRegisterAccess(mess);
        }
    }

    public static RegisterType getIndex(String index) throws IllegalRegisterAccess, IllegalValue {
        int i = 0;
        try {
            i = Integer.parseInt(index, 2);
        } catch (NumberFormatException e) {
            String mess = String.format("Index register index: %s is not binary", i);
            throw new IllegalValue(mess);
        }
        return getIndex(i);
    }

    public static RegisterType getIndex(int index) throws IllegalRegisterAccess {
        switch (index) {
            case 1:
                return X1;
            case 2:
                return X2;
            case 3:
                return X3;
            default:
                String mess = String.format("Index register index: %d out of bounds.", index);
                throw new IllegalRegisterAccess(mess);
        }
    }

    public static RegisterType getFloatingPoint(String index) throws IllegalRegisterAccess {
        int i = 0;
        try {
            i = Integer.parseInt(index, 2);
        } catch (NumberFormatException e) {
            String mess = String.format("Floating Point register index: %s is not binary", i);
            throw new IllegalValue(mess);
        }
        return getFloatingPoint(i);
    }

    public static RegisterType getFloatingPoint(int index) throws IllegalRegisterAccess {
        switch (index) {
            case 0:
                return FR0;
            case 1:
                return FR1;
            default:
                String mess = String.format("Floating Point register index: %d out of bounds.", index);
                throw new IllegalRegisterAccess(mess);
        }
    }
}
