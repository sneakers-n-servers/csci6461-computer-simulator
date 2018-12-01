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
import edu.gw.csci.simulator.utils.FloatingPointConvert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;

public class FloatingPointVector {

    private static final Logger LOGGER = LogManager.getLogger(FloatingPointVector.class);

    public static class FADD extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            BitSet RegisterBit = FR.getData();
            BitSet MemoryBit = memory.fetch(EA);

            float RegisterValue = FloatingPointConvert.FloatConvert(RegisterBit);
            float MemoryValue = FloatingPointConvert.FloatConvert(MemoryBit);

            if(RegisterValue==0){
                FR.setData(MemoryBit);
            }
            else if(MemoryValue==0){
                FR.setData(RegisterBit);
            }
            else {
                FR.setData(FloatingPointConvert.FloatConvert(RegisterValue+MemoryValue));
            }
            String mess = String.format("FADD: %s = %f + %f = %f",FR.getName(),RegisterValue,MemoryValue,RegisterValue+MemoryValue);
            LOGGER.info(mess);
        }
    }

    public static class FSUB extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            BitSet RegisterBit = FR.getData();
            BitSet MemoryBit = memory.fetch(EA);

            float RegisterValue = FloatingPointConvert.FloatConvert(RegisterBit);
            float MemoryValue = FloatingPointConvert.FloatConvert(MemoryBit);

            if(RegisterValue==0){
                FR.setData(FloatingPointConvert.FloatConvert(-MemoryValue));
            }
            else if(MemoryValue==0){
                FR.setData(RegisterBit);
            }
            else {
                FR.setData(FloatingPointConvert.FloatConvert(RegisterValue-MemoryValue));
            }
            String mess = String.format("FSUB: %s = %f - %f = %f",FR.getName(),RegisterValue,MemoryValue,RegisterValue-MemoryValue);
            LOGGER.info(mess);
        }
    }

    public static class VADD extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            int size = (int) FloatingPointConvert.FloatConvert(FR.getData());
            int Vector1 = BitConversion.convert(memory.fetch(EA));
            int Vector2 = BitConversion.convert(memory.fetch(EA+1));
            BitSet Vector1i,Vector2i;

            for (int i = 0; i < size; i++) {
                Vector1i = memory.fetch(Vector1+i);
                Vector2i = memory.fetch(Vector2+i);
                memory.store(Vector1+i, BinaryCalculate.BitAdd(Vector1i,Vector2i));
            }
            String mess = String.format("VADD: Vector Size:%d, Vector1 starts from %d, Vector2 starts from %d",
                    size,Vector1,Vector2);
            LOGGER.info(mess);
        }
    }

    public static class VSUB extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            int size = (int) FloatingPointConvert.FloatConvert(FR.getData());
            int Vector1 = BitConversion.convert(memory.fetch(EA));
            int Vector2 = BitConversion.convert(memory.fetch(EA+1));
            BitSet Vector1i,Vector2i;

            for (int i = 0; i < size; i++) {
                Vector1i = memory.fetch(Vector1+i);
                Vector2i = memory.fetch(Vector2+i);
                memory.store(Vector1+i, BinaryCalculate.BitMinus(Vector1i,Vector2i));
            }
            String mess = String.format("VSUB: Vector Size:%d, Vector1 starts from %d, Vector2 starts from %d",
                    size,Vector1,Vector2);
            LOGGER.info(mess);
        }
    }

    public static class CNVRT extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            Register FR0 = registers.getRegister(RegisterType.FR0);
            int RegisterValue = BitConversion.convert(R.getData());
            //The r register contains the value of F before the instruction is executed.
            int EA = memory.EA();
            BitSet MemoryBit = memory.fetch(EA);
            String mess;
            if(RegisterValue==0){
                //If F = 0, convert c(EA) to a fixed point number and store in r
                float MemoryValue = FloatingPointConvert.FloatConvert(MemoryBit);
                R.setData(BitConversion.convert((int)MemoryValue));
                mess = String.format("Convert %f to %d, store in %s",MemoryValue,(int)MemoryValue,R.getName());
            }
            else if (RegisterValue==1){
                //If F = 1, convert c(EA) to a floating point number and store in FR0.
                int MemoryValue = BitConversion.convert(MemoryBit);
                FR0.setData(FloatingPointConvert.FloatConvert((float)MemoryValue));
                mess = String.format("Convert %d to %f, store in %s",MemoryValue,(float)MemoryValue,FR0.getName());
            }
            else{
                mess = "The r register should contain the value of F before the instruction is executed.";
            }
            LOGGER.info(mess);
        }
    }

    public static class LDFR extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            int MemoryValue1 = BitConversion.convert(memory.fetch(EA));
            float MemoryValue2 = FloatingPointConvert.DecimalRepresentationConvert(memory.fetch(EA+1));
            float f = MemoryValue1+MemoryValue2;
            FR.setData(FloatingPointConvert.FloatConvert(f));

            String mess = String.format("LDFR: Load %f from memory to %s",f,FR.getName());
            LOGGER.info(mess);
        }
    }

    public static class STFR extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String FRs = getData().substring(0, 2);
            Register FR = registers.getRegister(RegisterType.getFloatingPoint(FRs));
            int EA = memory.EA();
            float f = FloatingPointConvert.FloatConvert(FR.getData());
            int MemoryValue1 = (int) f;
            float MemoryValue2 = f -MemoryValue1;

            memory.store(EA,BitConversion.convert(MemoryValue1));
            memory.store(EA+1,FloatingPointConvert.DecimalRepresentationConvert(MemoryValue2));

            String mess = String.format("STFR: Store %f to memory from %s",f,FR.getName());
            LOGGER.info(mess);
        }
    }
}
