package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.exceptions.IllegalMemoryAccess;
import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;
import edu.gw.csci.simulator.registers.AllRegisters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class AllMemoryTest {

    private Memory memory;
    private AllRegisters registers;
    private MemoryCache memoryCache;

    public AllMemoryTest(){
        this.memory = new Memory();
        this.registers = new AllRegisters();
    }

    @Before
    public void before(){
        this.memoryCache = new MemoryCache();
        memory.initialize();
        registers.initialize();
    }

    @Test(expected = IllegalMemoryAccess.class)
    public void testIllegalAccess1(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.store(5, new BitSet());
    }

    @Test(expected = MemoryOutOfBounds.class)
    public void testOutOfBounds1(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.store(memory.getSize(), new BitSet());
    }

    @Test(expected = IllegalMemoryAccess.class)
    public void testIllegalAccess2(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.fetch(5);
    }

    @Test(expected = MemoryOutOfBounds.class)
    public void testOutOfBounds2(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.fetch(memory.getSize());
    }

    @Test
    public void testCache(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.fetch(7);
        Assert.assertEquals(1, memoryCache.getTotalRequests());
    }
}