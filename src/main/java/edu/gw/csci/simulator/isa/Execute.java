package edu.gw.csci.simulator.isa;


import edu.gw.csci.simulator.convert.Bits;
import edu.gw.csci.simulator.memory.Memory;
import edu.gw.csci.simulator.memory.MemoryDecorator;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;

public class Execute {

	public static void excute_IR(AllRegisters allregisters, Memory memory) {
        /**
         * to excute the instructions in IR
         */
	    Register IR = allregisters.getRegister(RegisterType.IR);
	    RegisterDecorator irRegisterDecorator = new RegisterDecorator(IR);
	    String instruction = irRegisterDecorator.toBinaryString();

		String Opcode = instruction.substring(0,6);
		String R_code = instruction.substring(6,8);
		String IX_code = instruction.substring(8,10);
		String I_code = instruction.substring(10,11);
		String Address_code = instruction.substring(11,16);
		int EA = 0;
		boolean status = true;
		EA = Decode.EA (Opcode, IX_code,I_code,Address_code,memory,allregisters);

		Register PC = allregisters.getRegister(RegisterType.PC);
		Register R = allregisters.getRegister(Decode.R_code_decode(R_code));
		Register X = allregisters.getRegister(Decode.IX_code_decode(IX_code));

        MemoryDecorator memoryDecorator = new MemoryDecorator(memory, allregisters);
        RegisterDecorator rRegisterDecorator = new RegisterDecorator(R);
        RegisterDecorator xRegisterDecorator = new RegisterDecorator(X);

        switch (Decode.opcode_decode(Opcode)) {
            case "LDR":
                R.setData(memoryDecorator.fetch(EA));
                break;
            case "STR":
                memoryDecorator.store(rRegisterDecorator.toInt(), EA);
                break;
            case "LDA":
                rRegisterDecorator.setRegister(EA);
                break;
            case "LDX":
                X.setData(memoryDecorator.fetch(EA));
                break;
            case "STX":
                memoryDecorator.store(xRegisterDecorator.toInt(), EA);
                break;
            default:
                status = false;
                break;
        }
		if(status) {
			RegisterDecorator pcDecorator = new RegisterDecorator(PC);
			PC.setData(Bits.convert(pcDecorator.toInt()+1)); //PC+1
		}
		allregisters.logRegisters();
	}
}
