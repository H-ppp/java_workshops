package com.example.lr1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Cache<K, V> {
    private final Map<K, V> cache = Collections.synchronizedMap(new HashMap<>());

    public boolean contain(K key) {
        return cache.containsKey(key);
    }

    public void saveInCache(K key, V vals) {
        cache.put(key, vals);
    }

    public V getFromCache(K key) {
        return cache.get(key);
    }
}
