package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.isa.Decoder;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
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
            //Handle the dirty work
            LOGGER.info("Halt");
            if(cpu.TrapFlag){
                cpu.TrapFlag = false;
                Register PC = registers.getRegister(RegisterType.PC);
                LOGGER.info("Trap instruction return to memory[2]");
                //if the trap instructions meet hlt, return to memory[2]
                PC.setData(memory.fetch(2));
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
        private Decoder decoder;

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            this.decoder = new Decoder();

            LOGGER.info("Trap");
            Register PC = registers.getRegister(RegisterType.PC);
            int PCindex = BitConversion.convert(PC.getData());
            memory.store(PCindex+1,2);

            String TrapCode = data.substring(6,10);
            //trap code is the index into table
            int TrapTableIndex = BitConversion.fromBinaryString(TrapCode);

            //M0 is the startIndex of table
            int TableStartIndex = BitConversion.convert(memory.fetch(0));

            int InstructionIndexInTable = TrapTableIndex+TableStartIndex;

            //the Address of the routine to execute is stored in memory[M0+trapCode]
            int InstructionAddress = BitConversion.convert(memory.fetch(InstructionIndexInTable));

            PC.setData(BitConversion.convert(InstructionAddress));
            cpu.TrapFlag = true;

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
