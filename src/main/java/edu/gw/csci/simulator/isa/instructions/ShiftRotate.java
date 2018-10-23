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

public class ShiftRotate {
    private static final Logger LOGGER = LogManager.getLogger(ShiftRotate.class);

    public static class SRC implements Instruction {

        private InstructionType instructionType = InstructionType.SRC;

        private String data;

        @Override
        public void execute(AllMemory memory, AllRegisters registers) {
            String Rs = data.substring(0,2);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);
            String LR = data.substring(2,3);
            String AL = data.substring(3,4);
            String Counts = data.substring(6,9);
            boolean LorR = LR.equals("1");
            boolean AorL = AL.equals("1");
            int count = Integer.parseInt(Counts,2);

            LOGGER.info("SRC");
            if(count == 0){
                addPC.PCadder(registers);
            }
            else{
                addPC.PCadder(registers);
                String s1 = BitConversion.toBinaryString(R.getData(),R.getSize());
                if(LorR) {
                    //logically left shift equals to arithmetically left shift
                    String s2 = s1.substring(count);
                    for (int i = 0; i < count; i++) {
                        s2=s2+"0";
                    }
                    R.setData(BitConversion.convert(s2));
                }
                else if((!LorR)&&AorL){
                    //logically right shift
                    String s2 = s1.substring(0,s1.length()-count);
                    for (int i = 0; i < count; i++) {
                        s2="0"+s2;
                    }
                    R.setData(BitConversion.convert(s2));
                }
                else if((!LorR)&&(!AorL)){
                    //arithmetically right shift
                    String s2 = s1.substring(0,s1.length()-count);
                    for (int i = 0; i < count; i++) {
                        s2=s1.substring(0,1)+s2;
                    }
                    R.setData(BitConversion.convert(s2));
                }
            }
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
            LOGGER.info("RRC");

            String Rs = data.substring(0,2);
            Register R =registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);

            String LR = data.substring(2,3);
            boolean LorR = LR.equals("1");
            String Counts = data.substring(6,9);

            int count = Integer.parseInt(Counts,2);
            if(count ==0) {
                addPC.PCadder(registers);
            }
            else {
                addPC.PCadder(registers);
                String s1 = BitConversion.toBinaryString(R.getData(),R.getSize());
                if(LorR)
                //left rotation
                {
                    String s2 = s1.substring(count);
                    String s3 = s1.substring(0,count);
                    R.setData(BitConversion.convert(s2+s3));
                }
                else{
                    //right rotation
                    String s2 = s1.substring(0,s1.length()-count);
                    String s3 = s1.substring(s1.length()-count);
                    R.setData(BitConversion.convert(s3+s2));
                }

            }
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
