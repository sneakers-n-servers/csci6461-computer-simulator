package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.isa.addPC;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Transfer {

    private static final Logger LOGGER = LogManager.getLogger(Miscellaneous.class);

    public static class JZ implements Instruction {

        private InstructionType instructionType = InstructionType.HLT;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            String Rs = data.substring(0,2);
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA = memory.EA();
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            if(R.getData().isEmpty()){
                PCd.setRegister(EA);
            }
            else{
                addPC.PCadder(registers);
            }

            String mess = String.format("JZ Jump to %d when %s =0",EA,R.getName());
            LOGGER.info(mess);
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

    public static class JNE implements Instruction {

        private InstructionType instructionType = InstructionType.JNE;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            String Rs = data.substring(0,2);
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA = memory.EA();
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            if(!R.getData().isEmpty()){
                PCd.setRegister(EA);
            }
            else{
                addPC.PCadder(registers);
            }

            String mess = String.format("JNE Jump to %d when %s !=0",EA,R.getName());
            LOGGER.info(mess);
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

    public static class JCC implements Instruction {

        private InstructionType instructionType = InstructionType.JCC;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA = memory.EA();

            Register CC = registers.getRegister(RegisterType.CC);
            int bit = BitConversion.getBit(CC.getData());
            if(bit ==1){
                PCd.setRegister(EA);
            }
            else{
                addPC.PCadder(registers);
            }
            String mess =String.format("JCC Jump to %d if CC bit =1", EA);
            LOGGER.info(mess);
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

    public static class JMA implements Instruction {

        private InstructionType instructionType = InstructionType.JMA;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA =memory.EA();

            PCd.setRegister(EA);

            String mess = String.format("JMA Unconditional Jump To Address:%d",EA);
            LOGGER.info(mess);
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

    public static class JSR implements Instruction {

        private InstructionType instructionType = InstructionType.JSR;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            Register R3 = registers.getRegister(RegisterType.R3);
            RegisterDecorator R3d = new RegisterDecorator(R3);
            int EA= memory.EA();


            R3d.setRegister(BitConversion.convert(PC.getData())+1);
            PCd.setRegister(EA);

            String mess = String.format("JSR Jump to %d and Save Return Address:%d",
                    EA,BitConversion.convert(PC.getData())+1);
            LOGGER.info(mess);
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

    public static class RFS implements Instruction {

        private InstructionType instructionType = InstructionType.RFS;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            Register PC = registers.getRegister(RegisterType.PC);
            Register R0 = registers.getRegister(RegisterType.R3);
            RegisterDecorator R0d = new RegisterDecorator(R0);
            Register R3 = registers.getRegister(RegisterType.R3);

            int EA= memory.EA();


            R0d.setRegister(EA);
            PC.setData(R3.getData());

            String mess = String.format("RFS Return from subroutine, R0=%d PC=R3=%d",
                    EA,BitConversion.convert(R3.getData()));
            LOGGER.info(mess);
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

    public static class SOB implements Instruction {

        private InstructionType instructionType = InstructionType.SOB;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            String Rs = data.substring(0,2);
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA = memory.EA();
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);


            Rd.setRegister(BitConversion.convert(R.getData())-1);
            if(BitConversion.convert(R.getData())>0){
                PCd.setRegister(EA);
            }
            else{
                addPC.PCadder(registers);
            }

            String mess = String.format("SOB %s = %s -1 =%d, if %s>0, jump to %d",
                    R.getName(),R.getName(),BitConversion.convert(R.getData())-1,R.getName(),EA);
            LOGGER.info(mess);
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

    public static class JGE implements Instruction {

        private InstructionType instructionType = InstructionType.JGE;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            String Rs = data.substring(0,2);
            Register PC = registers.getRegister(RegisterType.PC);
            RegisterDecorator PCd = new RegisterDecorator(PC);
            int EA = memory.EA();
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);


            Rd.setRegister(BitConversion.convert(R.getData())-1);

            if(BitConversion.convert(R.getData())>=0){
                PCd.setRegister(EA);
            }
            else{
                addPC.PCadder(registers);
            }

            String mess = String.format("JGE if %s(%d) >=0, jump to %d",
                    R.getName(),BitConversion.convert(R.getData()),EA);
            LOGGER.info(mess);
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
