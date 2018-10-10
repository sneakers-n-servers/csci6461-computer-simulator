package edu.gw.csci.simulator.memory;

import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

public class MemoryCacheTest {

    @Test
    public void test1(){
       MemoryCache cache = new MemoryCache(16);
       BitSet b = new BitSet(7);
       for(int i = 0; i < 16; i++){
           cache.put(i, b);
           cache.get(i);
       }
       Assert.assertEquals(16, cache.getTotalRequests());
       Assert.assertEquals(16, cache.getCacheHit());
    }
}