package edu.gw.csci.simulator.isa.instructions;

import edu.gw.csci.simulator.cpu.CPU;
import edu.gw.csci.simulator.isa.Instruction;
import edu.gw.csci.simulator.memory.AllMemory;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterDecorator;
import edu.gw.csci.simulator.registers.RegisterType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadStore {

    private static final Logger LOGGER = LogManager.getLogger(LoadStore.class);

    public static class LDR extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            R.setData(memory.fetch(memory.EA()));
            //registers.PCadder();

            LOGGER.info("LDR");
            logger(getData(), registers);
        }
    }

    public static class STR extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));

            memory.store(memory.EA(), R.getData());
            //registers.PCadder();

            LOGGER.info("STR");
            logger(getData(), registers);
        }
    }

    public static class LDA extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            LOGGER.info("LDA");
            logger(getData(), registers);

            String Rs = getData().substring(0, 2);
            Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));
            RegisterDecorator Rd = new RegisterDecorator(R);

            Rd.setIntegerValue(memory.EA());
            //registers.PCadder();
        }
    }

    public static class LDX extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Xs = getData().substring(2, 4);
            Register X = registers.getRegister(RegisterType.getIndex(Xs));

            X.setData(memory.fetch(memory.EA()));
            //registers.PCadder();

            LOGGER.info("LDX");
            logger(getData(), registers);
        }
    }

    public static class STX extends Instruction {

        @Override
        public void execute(AllMemory memory, AllRegisters registers, CPU cpu) {
            String Xs = getData().substring(2, 4);
            Register X = registers.getRegister(RegisterType.getIndex(Xs));

            memory.store(memory.EA(), X.getData());
            //registers.PCadder();

            LOGGER.info("STX");
            logger(getData(), registers);
        }
    }

    private static void logger(String data, AllRegisters registers) {
        String Rs = data.substring(0, 2);
        String Xs = data.substring(2, 4);
        String Is = data.substring(4, 5);
        String I;
        if (Is.equals("0")) {
            I = "direct";
        } else {
            I = "indirect";
        }
        String AddressCode = data.substring(5);
        Register R = registers.getRegister(RegisterType.getGeneralPurpose(Rs));
        RegisterDecorator Rd = new RegisterDecorator(R);
        String xName = "null";
        if (!Xs.equals("00")) {
            Register X = registers.getRegister(RegisterType.getIndex(Xs));
            xName = X.getName();
        }

        String mess = String.format("R:%s IX:%s I:%s Addr:%s(%d)",
                R.getName(), xName, I, AddressCode, Integer.parseInt(AddressCode, 2));
        LOGGER.info(mess);
    }
}
