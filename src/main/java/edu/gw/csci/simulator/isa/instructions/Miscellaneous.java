package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.cpu.TrapController;
import edu.gw.csci.simulator.exceptions.SimulatorException;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Miscellaneous {

    private static final Logger LOGGER = LogManager.getLogger(Miscellaneous.class);

    public static class HLT implements Instruction {

        private InstructionType instructionType = InstructionType.HLT;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            LOGGER.info("HLT");
            //Thats it
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
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) throws SimulatorException {
            LOGGER.info("TRAP");
            String trapString = data.substring(data.length() - 4);
            int trapCode = BitConversion.fromBinaryStringToInt(trapString);
            throw TrapController.getRoutineException(trapCode);
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
