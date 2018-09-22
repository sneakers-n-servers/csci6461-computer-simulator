package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.convert.Bits;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterType;

import java.util.BitSet;

public class MemoryDecorator {

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
        char word_truth = '1';
        if(index >= 6 && index <= 2047){
            /**
             * 0-5 memory is Reserved for other use;
             */
            String BinaryValue = Integer.toBinaryString(value);
            //word is over 16 bits
            if (BinaryValue.length()>16) {
                return;
            }
            BitSet store = new BitSet(memory.getWordSize());
            for(int i = 0; i < BinaryValue.length() ; i++) {
                //System.out.println(BinaryValue.charAt(i));
                if(BinaryValue.charAt(i) == word_truth) {
                    store.set(BinaryValue.length()-i-1);
                }
            }
            memory.set(index, store);
            allRegisters.setRegister(RegisterType.MBR, store);
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
            //fault
            return this.memory.get(0);
        }
    }
}
