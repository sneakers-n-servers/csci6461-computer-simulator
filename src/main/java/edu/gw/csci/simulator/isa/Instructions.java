package edu.gw.csci.simulator.isa;

import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Instructions {

    private static final Logger LOGGER = LogManager.getLogger(Instructions.class);

    public static class HLT implements Instruction{

        private InstructionType instructionType = InstructionType.HLT;

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
    }

    public static class LDR implements Instruction{

        private InstructionType instructionType = InstructionType.LDR;

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
    }

    public static class STR implements Instruction{

        private InstructionType instructionType = InstructionType.STR;

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
    }

    public static class LDA implements Instruction{

        private InstructionType instructionType = InstructionType.LDA;

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
    }

    public static class LDX implements Instruction{

        private InstructionType instructionType = InstructionType.LDX;

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
    }

    public static class STX implements Instruction{

        private InstructionType instructionType = InstructionType.STX;

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
    }
}
