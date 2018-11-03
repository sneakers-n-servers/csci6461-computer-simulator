package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.exceptions.*;
import edu.gw.csci.simulator.isa.Decoder;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.registers.Register;
import edu.gw.csci.simulator.registers.RegisterType;
import edu.gw.csci.simulator.utils.BitConversion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

public class AllMemoryTest {

    private final Memory memory;
    private final AllRegisters registers;
    private MemoryCache memoryCache;
    private Decoder decoder;

    public AllMemoryTest(){
        this.memory = new Memory();
        this.registers = new AllRegisters();
        this.decoder = new Decoder();
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

    @Test(expected = IllegalMemoryAccess.class)
    public void testIllegalAccess2(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.fetch(5);
    }

    @Test()
    public void testIllegalAccess3(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        //allMemory.reservedStore(1,new BitSet());
        allMemory.store(2, new BitSet(),false);
    }

    @Test(expected = IllegalOpcode.class)
    public void testIllegalOpcode1(){
        BitSet bits = BitConversion.convert("1111110000000000");
        decoder.getInstruction(bits);
    }
    @Test(expected = IllegalValue.class)
    public void testIllegalOpcode2(){
        String X = "23";
        registers.getRegister(RegisterType.getGeneralPurpose(X));
    }

    @Test(expected = IllegalRegisterAccess.class)
    public void testIllegalOpcode3(){
        String X = "00";
        registers.getRegister(RegisterType.getIndex(X));
    }

    @Test(expected = MemoryOutOfBounds.class)
    public void testOutOfBounds1(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.store(memory.getSize(), new BitSet());
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

    @Test
    public void testCache2(){
        AllMemory allMemory = new AllMemory(memory, registers, memoryCache);
        allMemory.store(7, new BitSet());
        allMemory.fetch(7);
        Assert.assertEquals(1, memoryCache.getCacheHit());
    }
}