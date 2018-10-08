package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Instructions {

    private static final Logger LOGGER = LogManager.getLogger(Instructions.class);

    public static class HLT implements Instruction{

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            //Handle the dirty work
            LOGGER.info("Halt instruction got data " + data);
        }

        @Override
        public void setData(String data) {
            this.data = data;
        }
    }

    public static class TRAP implements Instruction{

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            LOGGER.info("Trap instruction got data " + data);
        }

        @Override
        public void setData(String data) {
            this.data = data;
        }
    }
}
