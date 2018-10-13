package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.isa.IOInstruction;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IO {

    private static final Logger LOGGER = LogManager.getLogger(IO.class);


    public static class IN implements IOInstruction {

        private InstructionType instructionType = InstructionType.IN;

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

        @Override
        public InstructionType getInstructionType() {
            return instructionType;
        }

        @Override
        public void setConsole(TextArea textArea) {

        }
    }

    public static class OUT implements IOInstruction {

        private InstructionType instructionType = InstructionType.OUT;
        private TextArea console;

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

        @Override
        public InstructionType getInstructionType() {
            return instructionType;
        }

        @Override
        public void setConsole(TextArea textArea) {
            this.console = textArea;
        }
    }
}
