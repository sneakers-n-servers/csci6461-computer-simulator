package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.exceptions.IllegalTrapCode;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;

public class Miscellaneous {

    private static final Logger LOGGER = LogManager.getLogger(Miscellaneous.class);

    public static class HLT implements Instruction {

        private InstructionType instructionType = InstructionType.HLT;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            LOGGER.info("HLT");
            if(cpu.TrapFlag){
                cpu.TrapFlag =false;
                Register PC = registers.getRegister(RegisterType.PC);
                RegisterDecorator PCd = new RegisterDecorator(PC);
                //when trap meets hlt, return to memory[2]
                LOGGER.info("Trap instruction return to memory[2]");
                int PCindex = BitConversion.convert(memory.fetch(2,false));
                PCd.setValue(PCindex);
            }
        }

        @Override
        public void setData(String data) {
            this.data = data;
        }

        @Override
        public InstructionType getInstructionType() {
            return instructionType;
        }
    }

    public static class TRAP implements Instruction {

        private InstructionType instructionType = InstructionType.TRAP;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            LOGGER.info("TRAP");
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int PCplus1 = BitConversion.convert(PC.getData())+1;
            //store the return PC+1 of trap instruction to memory[2]
            memory.store(2,BitConversion.convert(PCplus1),false);

            String TrapCode = data.substring(6,10);
            //TrapCode is the index in the table
            int TableIndex = Integer.parseInt(TrapCode,2);

            //M0 is the start of the table
            int TableStart = BitConversion.convert(memory.fetch(0,false));
            //get the memory index
            int index = TableIndex+TableStart;
            //the start address of the routine is in the index
            BitSet address = memory.fetch(index);

            if(address.isEmpty()){
                String mess = "Illegal TrapCode: There is no address in the trap table.";
                throw  new IllegalTrapCode(mess);
            }
            else {
                int PCindex = BitConversion.convert(address);
                PCd.setValue(PCindex-1);
                //PC.setData(address);
                cpu.TrapFlag = true;
            }
        }

        @Override
        public void setData(String data) {
            this.data = data;
        }

        @Override
        public InstructionType getInstructionType() {
            return instructionType;
        }
    }
}
