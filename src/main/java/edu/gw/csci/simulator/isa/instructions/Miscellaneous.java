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

    public static class HLT extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            LOGGER.info("HLT");
        }
    }

    public static class TRAP extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) throws SimulatorException {
            LOGGER.info("TRAP");
            String trapString = getData().substring(getData().length() - 4);
            int trapCode = BitConversion.fromBinaryStringToInt(trapString);
            throw TrapController.getRoutineException(trapCode);
        }
    }
}
