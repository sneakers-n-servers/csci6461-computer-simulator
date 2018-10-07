package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;

/**
 * Provides abstraction of support operations for interaction between the simulated memory and the GUI framework.
 *
 * @version 20180918
 */
public class AllMemory {

    private static final Logger LOGGER = LogManager.getLogger(AllMemory.class);

    private final Memory memory;
    private final AllRegisters allRegisters;

    public AllMemory(Memory memory, AllRegisters allRegisters) {
        this.memory = memory;
        this.allRegisters = allRegisters;
    }

    public void store(int value, int index) {
        if (index >= 6 && index <= 2047) {
            // 0-5 memory is Reserved for other use;
            String mess = String.format("Stroing %d to memory index %d", value, index);
            LOGGER.info(mess);
            String BinaryValue = Integer.toBinaryString(value);
            //word is over 16 bits
            if (BinaryValue.length() > 16) {
                mess = "Word is over 16 bits.";
                LOGGER.error(mess);
                return;
            }
            BitSet store = BitConversion.convert(value);
            memory.set(index, store);
            allRegisters.setRegister(RegisterType.MBR, store);
        } else {
            String mess = "Wrong index.";
            LOGGER.info(mess);
        }
    }

    public BitSet fetch(int index) {
        //get memory with address
        //set MAR and MBR in every fetch
        if (index >= 0 && index <= 2047) {
            allRegisters.setRegister(RegisterType.MAR, BitConversion.convert(index));
            allRegisters.setRegister(RegisterType.MBR, memory.get(index));
            return memory.get(index);
        } else {
            LOGGER.error("Fetch failed.");
            return this.memory.get(0);
        }
    }
}
