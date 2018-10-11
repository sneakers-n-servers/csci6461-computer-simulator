package edu.gw.csci.simulator.memory;

import edu.gw.csci.simulator.utils.BitConversion;
import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

public class MemoryCacheTest {

    @Test
    public void testSimple() {
        int cacheSize = 16;
        MemoryCache cache = new MemoryCache(cacheSize);
        BitSet b = new BitSet(7);
        for (int i = 0; i < 16; i++) {
            cache.put(i, b);
            Assert.assertTrue(cache.get(i).isPresent());
        }
        Assert.assertEquals(cacheSize, cache.getTotalRequests());
        Assert.assertEquals(cacheSize, cache.getCacheHit());
    }

    @Test
    public void testEvict() {
        int cacheSize = 16;
        MemoryCache cache = new MemoryCache(16);
        for (int i = 0; i < 100; i++) {
            BitSet toCache = BitConversion.convert(i);
            cache.put(i, toCache);
        }
        Assert.assertEquals(cacheSize, cache.getSize());
        System.out.println("Checking cache contents");
        for (BitSet ret : cache.getCacheData()) {
            int i = BitConversion.convert(ret);
            System.out.println(i);
        }
    }

}