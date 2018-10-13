package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterType;

import java.util.BitSet;

public class SetCC {
    /**
     * SET CC OVERFLOW,UNDERFLOW,DIVZERO,EQUALORNOT
     */
    public static void OVERFLOW(AllRegisters registers){
        Register CC = registers.getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(3);
        CC.setData(bits);
    }

    public static void UNDERFLOW(AllRegisters registers){
        Register CC = registers.getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(2);
        CC.setData(bits);
    }

    public static void DIVZERO(AllRegisters registers){
        Register CC = registers.getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        bits.set(1);
        CC.setData(bits);
    }

    public static void EQUALORNOT(AllRegisters registers, boolean equal){
        Register CC = registers.getRegister(RegisterType.CC);
        BitSet bits = CC.getData();
        if(equal){
            bits.set(0);
        }
        else{
            bits.clear(0);
        }
        CC.setData(bits);
    }
}
