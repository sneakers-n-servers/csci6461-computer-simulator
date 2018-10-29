package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.exceptions.MemoryOutOfBounds;
import edu.gw.csci.simulator.exceptions.SimulatorException;
import edu.gw.csci.simulator.registers.AllRegisters;
import edu.gw.csci.simulator.utils.BitConversion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

public class MemoryCacheTest {

    private MemoryCache cache;

    @Before
    public void before(){
        cache = new MemoryCache();
    }

    @Test
    public void testSimple() {
        BitSet b = new BitSet(7);
        for (int i = 0; i < 16; i++) {
            cache.put(i, b);
            Assert.assertTrue(cache.get(i).isPresent());
        }
        Assert.assertEquals(cache.getSize(), cache.getTotalRequests());
        Assert.assertEquals(cache.getSize(), cache.getCacheHit());
    }

    @Test
    public void testEvict() {
        int numToPut = 100;
        for (int i = 0; i < numToPut; i++) {
            BitSet toCache = BitConversion.convert(i);
            cache.put(i, toCache);
        }
        int expected = numToPut - cache.getMaxCacheSize();
        for (BitSet ret : cache.getCacheData()) {
            int i = BitConversion.convert(ret);
            Assert.assertEquals(expected, i);
            expected++;
        }
    }

}