package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.isa.Decode;
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

public class ArithmeticLogic {

    private static final Logger LOGGER = LogManager.getLogger(ArithmeticLogic.class);

    public static class AMR implements Instruction {

        private InstructionType instructionType = InstructionType.AMR;

        private int rxCode;
        private int ryCode;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            Register rx = registers.getRegister(RegisterType.getGeneralPurpose(this.rxCode));
            Register ry = registers.getRegister(RegisterType.getGeneralPurpose(this.ryCode));
            RegisterDecorator rxDecorator = new RegisterDecorator(rx);
            RegisterDecorator ryDecorator = new RegisterDecorator(ry);

            int EA = Decode.EA(memory, registers);
            int RxValue = rxDecorator.toInt();
            int MemoryValue = BitConversion.convert(memory.fetch(EA));
            if(ryDecorator.toInt() + MemoryValue>=Math.pow(2,16)){
                //SetCC.OVERFLOW(registers);
            }
            rxDecorator.setRegister(RxValue+MemoryValue);

            String mess = String.format("AMR R:%s EA:%d, %s = %d + %d",
                    rx.getName(),EA,ry.getName(),RxValue,MemoryValue);
            LOGGER.info(mess);
        }

        @Override
        public void setData(String data) {
            this.rxCode = BitConversion.fromBinaryString(data.substring(6, 8));
            this.ryCode = BitConversion.fromBinaryString(data.substring(8, 10));
        }

        @Override
        public InstructionType getInstructionType() {
            return instructionType;
        }
    }

    public static class SMR implements Instruction {

        private InstructionType instructionType = InstructionType.SMR;

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

    public static class AIR implements Instruction {

        private InstructionType instructionType = InstructionType.AIR;

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

    public static class SIR implements Instruction {

        private InstructionType instructionType = InstructionType.SIR;

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

    public static class MLT implements Instruction {

        private InstructionType instructionType = InstructionType.MLT;

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

    public static class DVD implements Instruction {

        private InstructionType instructionType = InstructionType.DVD;

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

    public static class TRR implements Instruction {

        private InstructionType instructionType = InstructionType.TRR;

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

    public static class AND implements Instruction {

        private InstructionType instructionType = InstructionType.AND;

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

    public static class ORR implements Instruction {

        private InstructionType instructionType = InstructionType.ORR;

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

    public static class NOT implements Instruction {

        private InstructionType instructionType = InstructionType.NOT;

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

    public static class SRC implements Instruction {

        private InstructionType instructionType = InstructionType.SRC;

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

    public static class RRC implements Instruction {

        private InstructionType instructionType = InstructionType.RRC;

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
