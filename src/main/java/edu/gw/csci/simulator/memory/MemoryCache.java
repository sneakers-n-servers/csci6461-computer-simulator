package edu.gw.csci.simulator.memory;

import java.util.*;

public class MemoryCache {

    private final LinkedList<BitSet> cache;

    private final HashMap<Integer, Integer> lookup;

    private final static int DEFAULT_CACHE_SIZE = 16;
    private final int maxCacheSize;
    private long cacheHit = 0, cacheMiss =0, totalRequests = 0;

    public MemoryCache(){
        this.cache = new LinkedList<>();
        this.lookup = new HashMap<>();
        this.maxCacheSize = DEFAULT_CACHE_SIZE;
    }

    public MemoryCache(int cacheSize){
        this.cache = new LinkedList<>();
        this.lookup = new HashMap<>();
        this.maxCacheSize = cacheSize;
    }

    public void put(int memoryIndex, BitSet data){
        if(cache.size() == maxCacheSize){
            //Pop the first off the queue
            cache.removeFirst();

            //Remove the entry pointing to zero, and reduce downwards
            Integer removeIndex = null;
            for(Map.Entry<Integer, Integer> entry: lookup.entrySet()){
                if(entry.getValue() == 0){
                    removeIndex = entry.getKey();
                }else{
                    Integer newValue = entry.getValue() - 1;
                    lookup.put(entry.getKey(), newValue);
                }
            }
            lookup.remove(removeIndex);
        }
        cache.add(data);
        lookup.put(memoryIndex, cache.size() - 1);
    }

    public Optional<BitSet> get(int memoryIndex){
        totalRequests++;
        if(lookup.containsKey(memoryIndex)){
            cacheHit++;
            BitSet data = cache.get(lookup.get(memoryIndex));
            return Optional.of(data);
        }
        cacheMiss++;
        return Optional.empty();
    }

    public long getSize(){
        return this.cache.size();
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public List<BitSet> getCacheData(){
        List<BitSet> ret = new ArrayList<>();
        ret.addAll(cache);
        return ret;
    }
}
