package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.exceptions.IllegalMemoryAccess;
import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;
import java.util.Optional;

/**
 * Provides abstraction of support operations for interaction between the simulated memory and the GUI framework.
 *
 * @version 20180918
 */
public class AllMemory {

    private static final Logger LOGGER = LogManager.getLogger(AllMemory.class);

    private final Memory memory;
    private final AllRegisters allRegisters;
    private final MemoryCache memoryCache;

    private final static int highestReservedMemory = 5;
    private final int maxMemory;

    public AllMemory(Memory memory, AllRegisters allRegisters, MemoryCache memoryCache) {
        this.memory = memory;
        this.allRegisters = allRegisters;
        this.memoryCache = memoryCache;
        this.maxMemory = memory.getSize() - 1;
    }

    public void store(int value, int index) throws MemoryOutOfBounds, IllegalMemoryAccess {
        checkIndex(index, true);

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

    }

    /**
     * Fetches an address from memory. If the memory location is cached, it will return
     * the cached value, otherwise it will get the data from memory. The MAR is updated to
     * the index being fetched, and the MBR is updated with the value.
     *
     * @param index The index to fetch
     * @return The contents of memory, or cache, as appropriate.
     * @throws MemoryOutOfBounds   When the memory index is out of bounds
     * @throws IllegalMemoryAccess When the memory index is reserved
     */
    public BitSet fetch(int index) throws MemoryOutOfBounds, IllegalMemoryAccess {
        checkIndex(index, true);
        BitSet toFetch = BitConversion.convert(index);
        allRegisters.setRegister(RegisterType.MAR, toFetch);
        Optional<BitSet> bits = memoryCache.get(index);
        BitSet fetched = bits.orElseGet(() -> memory.get(index));
        allRegisters.setRegister(RegisterType.MBR, fetched);
        return fetched;
    }

    /**
     * Checks to make sure that the memory address is valid, and if so, sets the bits in memory.
     * The memory buffer register is updated to value set, and the data is placed in the cache.
     *
     * @param index  The index of the data to store
     * @param bitSet The data to store
     * @throws MemoryOutOfBounds   When the memory index is out of bounds
     * @throws IllegalMemoryAccess When the memory index is reserved
     */
    public void store(int index, BitSet bitSet) throws MemoryOutOfBounds, IllegalMemoryAccess {
        checkIndex(index, true);
        memory.set(index, bitSet);
        allRegisters.setRegister(RegisterType.MBR, bitSet);
        memoryCache.put(index, bitSet);
    }

    /**
     * We need to check to make sure that the memory being set, or fetched is a valid location.
     * Making sure that the memory is in bounds is something that should always occur, but during
     * the implementation of TRAP codes in part 3, there are circumstances where we need to access
     * the special memory locations. This is why the throwReserve flag exists
     *
     * @param index An index to check the validity of
     * @param throwReseve Whether or not to throw an exception if trying to access a reserved location
     * @throws MemoryOutOfBounds When the memory index is out of bounds
     * @throws IllegalMemoryAccess When the memory index is reserved
     */
    private void checkIndex(int index, boolean throwReseve) throws MemoryOutOfBounds, IllegalMemoryAccess {
        if(index <= highestReservedMemory){
            String mess = String.format("Will not store data in reserved location %d", index);
            LOGGER.error(mess);
            throw new MemoryOutOfBounds(mess);
        }
        if(index > maxMemory && throwReseve){
            String mess = String.format("Will not store index: %d higher than max: %d", index, maxMemory);
            LOGGER.error(mess);
            throw new IllegalMemoryAccess(mess);
        }
    }
}
