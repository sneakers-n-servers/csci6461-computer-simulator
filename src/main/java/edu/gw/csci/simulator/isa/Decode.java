package edu.gw.csci.simulator.isa;


import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;

import java.util.Optional;

/**
 * Provides decoding support to the instruction set.
 *
 * @version 20180920
 */
public class Decode {

//    public static Optional<RegisterType> R_code_decode(String R_code) {
//        for (RegisterType RCD : RegisterType.values()) {
//            /**
//             * Traversal enum RegisterType choose which General Purpose Register to use
//             */
//            if (R_code.equals(RCD.getBinaryCode()) && RCD.getDescription().equals("General Purpose Register")) {
//                return Optional.of(RCD);
//            }
//        }
//        return Optional.empty();
//    }
//
//    public static Optional<RegisterType> IX_code_decode(String IX_code) {
//        for (RegisterType IXC : RegisterType.values()) {
//            /**
//             * Traversal enum RegisterType choose which Index Register to use
//             */
//            if (IX_code.equals(IXC.getBinaryCode()) && IXC.getDescription().equals("Index Register")) {
//                return Optional.of(IXC);
//            }
//        }
//        return Optional.empty();
//    }
    /**
     *
     * calculate the effective address
     *
     */
    public static int EA(AllMemory allMemory, AllRegisters allRegisters) {

        int EA;
        Register IR = allRegisters.getRegister(RegisterType.IR);
        String instruction = BitConversion.toBinaryString(IR.getData(),16);
        String Opcode = instruction.substring(0, 6);
        //String R_code = instruction.substring(6, 8);
        String IX_code = instruction.substring(8, 10);
        String I_code = instruction.substring(10, 11);
        String Address_code = instruction.substring(11, 16);

//        if (Opcode.equals(InstructionType.RFS.getOpCode())) {
//            //I,IX is ignored in RFS
//            EA = Integer.parseInt(Address_code, 2);
//            return EA;
//        }
//        if (Opcode.equals(InstructionType.AIR.getOpCode()) || Opcode.equals(InstructionType.SIR.getOpCode())) {
//            //I,IX is ignored in AIR,SIR
//            EA = Integer.parseInt(Address_code, 2);
//            return EA;
//        }
        if (InstructionType.getInstructionType(Opcode).equals(InstructionType.LDX) ||
                InstructionType.getInstructionType(Opcode).equals(InstructionType.STX)) {
            //IX is not used in LDX,STX
            if (I_code.equals("0")) {
                return Integer.parseInt(Address_code, 2);
            }
            else {
                EA = BitConversion.convert(allMemory.fetch(Integer.parseInt(Address_code, 2)));
                return EA;
            }
        }
        if (I_code.equals("0")) {
            if (IX_code.equals("00")) {
                EA = Integer.parseInt(Address_code, 2);
                return EA;
            } else {
                //int ixCode = BitConversion.fromBinaryString(IX_code);
                RegisterType registerType = RegisterType.getIndex(IX_code);
                Register register = allRegisters.getRegister(registerType);
                RegisterDecorator Xd = new RegisterDecorator(register);
                EA = Xd.toInt() + Integer.parseInt(Address_code, 2);
                return EA;
            }
        }
        else {
            if (IX_code.equals("00")) {
                EA = BitConversion.convert(allMemory.fetch(Integer.parseInt(Address_code, 2)));
                return EA;
            } else {
                //int ixCode = BitConversion.fromBinaryString(IX_code);
                RegisterType registerType = RegisterType.getIndex(IX_code);
                Register register = allRegisters.getRegister(registerType);
                RegisterDecorator Xd = new RegisterDecorator(register);
                int index = Xd.toInt() + Integer.parseInt(Address_code, 2);
                EA = BitConversion.convert(allMemory.fetch(index));
                return EA;
            }
        }
    }
}
