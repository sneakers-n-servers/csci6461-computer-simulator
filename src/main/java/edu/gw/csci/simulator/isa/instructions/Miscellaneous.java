package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Miscellaneous {

    private static final Logger LOGGER = LogManager.getLogger(Miscellaneous.class);

    public static class HLT implements Instruction {

        private InstructionType instructionType = InstructionType.HLT;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            //Handle the dirty work
            LOGGER.info("Halt");
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
