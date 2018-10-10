package edu.gw.csci.simulator.memory;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class MemoryCacheTest {

    @Test
    public void test1(){
       MemoryCache cache = new MemoryCache(16);
       BitSet b = new BitSet(7);
       for(int i = 0; i < 20; i++){
           cache.put(i, b);
           cache.get(i);
       }
        System.out.println(cache.getSize());
    }
}