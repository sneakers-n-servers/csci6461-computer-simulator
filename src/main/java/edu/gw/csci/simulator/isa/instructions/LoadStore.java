package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.isa.Decode;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.isa.addPC;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadStore {

    private static final Logger LOGGER = LogManager.getLogger(LoadStore.class);

    public static class LDR implements Instruction {

        private InstructionType instructionType = InstructionType.LDR;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            //Handle the dirty work

            //LOGGER.info("LDR" + data);
            int EA = Decode.EA(memory,registers);
            String Rs = data.substring(0,2);
            String Xs = data.substring(2,4);
            String Is = data.substring(4,5);
            String AddressCode = data.substring(5);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);
            //Register X = registers.getRegister(Decode.IX_code_decode(Xs).get());
            //RegisterDecorator Xd = new RegisterDecorator(X);
            R.setData(memory.fetch(EA));
            String mess = String.format("LDR R:%s IX:%s I:%s Addr:%s",R.getName(),Xs,Is,AddressCode);
            LOGGER.info(mess);
            addPC.PCadder(registers);
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
            LOGGER.info("LDA instruction got data " + data);
            int EA=Decode.EA(memory, registers);
            String Rs = data.substring(0,2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);
            Rd.setRegister(EA);
            addPC.PCadder(registers);
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
