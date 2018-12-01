package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.isa.InstructionType;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BinaryCalculate;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShiftRotate {
    private static final Logger LOGGER = LogManager.getLogger(ShiftRotate.class);

    public static class SRC extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            String AL = getData().substring(2, 3);
            String LR = getData().substring(3, 4);
            String Counts = getData().substring(6, 10);

            boolean LorR = LR.equals("1");
            boolean AorL = AL.equals("1");
            int count = Integer.parseInt(Counts, 2);
            String LRflag;
            String ALflag;

            if (LorR) LRflag = "left";
            else LRflag = "right";

            if (AorL) ALflag = "logically";
            else ALflag = "arithmetically";

            String mess = String.format("SRC Shift Register:%s %s %s by %d", R.getName(), LRflag, ALflag, count);
            LOGGER.info(mess);
            if (count != 0) {
                //registers.PCadder();
                String s1 = BitConversion.toBinaryString(R.getData(), R.getSize());
                int value = BitConversion.convert(R.getData());
                if (LorR) {
                    //logically left shift equals to arithmetically left shift
                    s1 = BinaryCalculate.BinaryLeftShift(s1, count, registers);
                } else if (AorL) {
                    //logically right shift
                    s1 = BinaryCalculate.BinaryLogicRightShift(s1, count);
                } else {
                    //arithmetically right shift
                    s1 = BinaryCalculate.BinaryArithmeticalRightShift(s1, count);

                }

                R.setData(BitConversion.convert(s1));
            }
        }
    }

    public static class RRC extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {

            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            String LR = getData().substring(3, 4);
            boolean LorR = LR.equals("1");
            String Counts = getData().substring(6, 10);
            int count = Integer.parseInt(Counts, 2);//Integer.parseInt(Counts,2);
            String LRflag;

            if (LorR) LRflag = "left";
            else LRflag = "right";

            String mess = String.format("RRC Rotate Register:%s %s by %d", R.getName(), LRflag, count);
            LOGGER.info(mess);

            if (count != 0) {
                String s1 = BitConversion.toBinaryString(R.getData(), R.getSize());
                if (LorR)
                //left rotation
                {
                    s1 = BinaryCalculate.BinaryLeftRotate(s1, count);
                } else {
                    //right rotation
                    s1 = BinaryCalculate.BinaryRightRotate(s1, count);
                }

                R.setData(BitConversion.convert(s1));
            }
        }
    }
}
