package edu.gw.csci.simulator.isa;


import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;

/**
 * Provides decoding support to the instruction set.
 *
 * @version 20180920
 */
public class Decode {

    public static String opcode_decode(String Opcode) {
        for (InstructionType OCT : InstructionType.values()) {
            /**
             * Traversal enum OpCodeType choose which operation to do
             */
            if (Opcode.equals(OCT.getOpCode())) {
                return OCT.toString();
            }
        }
        return "";
    }

    public static RegisterType R_code_decode(String R_code) {
        for (RegisterType RCD : RegisterType.values()) {
            /**
             * Traversal enum RegisterType choose which General Purpose Register to use
             */
            if (R_code.equals(RCD.getBinaryCode()) && RCD.getDescription().equals("General Purpose Register")) {
                return RCD;
            }
        }
        return RegisterType.MFR;
    }

    public static RegisterType IX_code_decode(String IX_code) {
        for (RegisterType IXC : RegisterType.values()) {
            /**
             * Traversal enum RegisterType choose which Index Register to use
             */
            if (IX_code.equals(IXC.getBinaryCode()) && IXC.getDescription().equals("Index Register")) {
                return IXC;
            }
        }
        return RegisterType.MFR;
    }

    public static int EA(String Opcode, String IX_code, String I_code, String Address_code, Memory memory, AllRegisters allRegisters) {
        /**
         * calculate the effective address
         * LDX and STX don't use Index
         */
        if (Opcode.equals(InstructionType.LDX.getOpCode()) || Opcode.equals(InstructionType.STX.getOpCode())) {
            if (I_code.equals("0")) {
                //System.out.println(Integer.parseInt(Address_code,2));
                return Integer.parseInt(Address_code, 2);
            } else {
                //System.out.println(BitConversion.utils(m.fetch(Integer.parseInt(Address_code, 2))));
                AllMemory md = new AllMemory(memory, allRegisters);
                return BitConversion.convert(md.fetch(Integer.parseInt(Address_code, 2)));
            }
        }
        if (I_code.equals("0")) {
            if (IX_code.equals("00")) {
                return Integer.parseInt(Address_code, 2);
            } else {
                RegisterType registerType = Decode.IX_code_decode(IX_code);
                Register register = allRegisters.getRegister(registerType);
                RegisterDecorator rd = new RegisterDecorator(register);
                return rd.toInt() + Integer.parseInt(Address_code, 2);
            }
        } else {
            AllMemory md = new AllMemory(memory, allRegisters);
            if (IX_code.equals("00")) {
                return BitConversion.convert(md.fetch(Integer.parseInt(Address_code, 2)));
            } else {
                RegisterType registerType = Decode.IX_code_decode(IX_code);
                Register register = allRegisters.getRegister(registerType);
                RegisterDecorator rd = new RegisterDecorator(register);

                int index = rd.toInt() + Integer.parseInt(Address_code, 2);

                return BitConversion.convert(md.fetch(index));
            }
        }
    }
}
