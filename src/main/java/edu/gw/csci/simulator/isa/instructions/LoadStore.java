package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
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

public class LoadStore {

    private static final Logger LOGGER = LogManager.getLogger(LoadStore.class);

    public static class LDR implements Instruction {

        private InstructionType instructionType = InstructionType.LDR;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers,CPU cpu) {
            String Rs = data.substring(0,2);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            R.setData(memory.fetch(memory.EA()));
            registers.PCadder();

            LOGGER.info("LDR");
            logger(data,registers);
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
        public void execute(AllMemory memory, AllRegisters registers,CPU cpu) {
            String Rs = data.substring(0,2);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            memory.store(memory.EA(),R.getData());
            registers.PCadder();

            LOGGER.info("STR");
            logger(data,registers);
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
        public void execute(AllMemory memory, AllRegisters registers,CPU cpu) {
            LOGGER.info("LDA");
            logger(data,registers);

            String Rs = data.substring(0,2);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);

            Rd.setValue(memory.EA());
            registers.PCadder();
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
        public void execute(AllMemory memory, AllRegisters registers,CPU cpu) {
            String Xs = data.substring(2,4);
            Register X =registers.getRegister(RegisterType.getIndex(Xs));

            X.setData(memory.fetch(memory.EA()));
            registers.PCadder();

            LOGGER.info("LDX");
            logger(data,registers);
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
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Xs = data.substring(2,4);
            Register X =registers.getRegister(RegisterType.getIndex(Xs));

            memory.store(memory.EA(),X.getData());
            registers.PCadder();

            LOGGER.info("STX");
            logger(data,registers);
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

    private static void logger(String data, AllRegisters registers){
        String Rs = data.substring(0,2);
        String Xs = data.substring(2,4);
        String Is = data.substring(4,5);
        String I;
        if(Is.equals("0")){
            I = "direct";
        }
        else{
            I = "indirect";
        }
        String AddressCode = data.substring(5);
        Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
        RegisterDecorator Rd = new RegisterDecorator(R);
        String xName = "null";
        if(!Xs.equals("00")) {
            Register X = registers.getRegister(RegisterType.getIndex(Xs));
            xName = X.getName();
        }

        String mess = String.format("R:%s IX:%s I:%s Addr:%s(%d)",R.getName(),xName,I,AddressCode, BitConversion.fromBinaryString(AddressCode));
        LOGGER.info(mess);
    }
}
