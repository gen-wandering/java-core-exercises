package com.tasks.completed.expiringcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpiringCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();

    public void put(K key, V value, long expirationTimeMillis) {
        cache.put(key, new CacheEntry<>(value, expirationTimeMillis));
    }

    public V get(K key) {
        var entry = cache.get(key);
        if (entry.isExpired()) {
            evictExpiredEntry(key);
            return null;
        }
        return entry.getValue();
    }

    private void evictExpiredEntry(K key) {
        cache.remove(key);
    }

    private static class CacheEntry<V> {
        private final V value;
        private final long expirationTime;

        CacheEntry(V value, long expirationTimeMillis) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + expirationTimeMillis;
        }

        V getValue() {
            return value;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}