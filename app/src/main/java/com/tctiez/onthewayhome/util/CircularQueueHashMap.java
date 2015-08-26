package com.tctiez.onthewayhome.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class CircularQueueHashMap<K, V> {
    private static final float  mHashTableLoadFactor = 0.75f;
    private LinkedHashMap<K, V> mMap;
    private int                 mCacheSize;

    public CircularQueueHashMap(int cacheSize) {
        this.mCacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil(cacheSize / mHashTableLoadFactor) + 1;
        mMap = new LinkedHashMap<K, V>(hashTableCapacity, mHashTableLoadFactor, true) {
            private static final long serialVersionUID = 1;

            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > CircularQueueHashMap.this.mCacheSize;
            }
        };
    }

    public boolean containKey(Object key) {
        return mMap.containsKey(key);
    }

    public V get(K key) {
        return mMap.get(key);
    }

    public void put(K key, V value) {
        mMap.put(key, value);
    }

    public int usedEntries() {
        return mMap.size();
    }

    public Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<Map.Entry<K, V>>(mMap.entrySet());
    }
}