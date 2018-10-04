package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.utils.Bits;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;

  /**
   * Provides abstraction of support operations for interaction between the simulated memory and the GUI framework.
   *
   * @version 20180918
   */
public class MemoryDecorator {

    private static final Logger logger = LogManager.getLogger(MemoryDecorator.class);

    private final Memory memory;
    private final AllRegisters allRegisters;

    public MemoryDecorator(Memory memory, AllRegisters allRegisters){
        this.memory = memory;
        this.allRegisters = allRegisters;
    }


    public void store(int value, int index){
        /**
         * set memory[Addr] = Value
         * for example
         * Value = 8, BinaryValue = 1000, memory[Addr] = {3}
         * Value = 5, BinaryValue = 101,  memory[Addr] = {0, 2}
         * it shows which bit is '1'
         */
        if(index >= 6 && index <= 2047){
            /**
             * 0-5 memory is Reserved for other use;
             */
            String mess = String.format("Stroing %d to memory index %d", value, index);
            logger.info(mess);
            String BinaryValue = Integer.toBinaryString(value);
            //word is over 16 bits
            if (BinaryValue.length()>16) {
                mess = "Word is over 16 bits";
                logger.info(mess);
                return;
            }
            BitSet store = Bits.convert(value);
            memory.set(index, store);
            allRegisters.setRegister(RegisterType.MBR, store);
        }
        else{
            String mess = "Wrong index";
            logger.info(mess);
        }
    }

    public BitSet fetch(int index) {
        /**
         * get memory with address
         * set MAR and MBR in every fetch
         */
        if(index >= 0 && index <= 2047) {
            allRegisters.setRegister(RegisterType.MAR, Bits.convert(index));
            allRegisters.setRegister(RegisterType.MBR, memory.get(index));
            return memory.get(index);
        }
        else {
            return this.memory.get(0);
        }
    }
}
