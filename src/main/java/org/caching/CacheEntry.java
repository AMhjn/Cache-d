package org.caching;

public class CacheEntry<V> {
    private final V value;
    private final long expiryTime;

    public CacheEntry(V value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        return expiryTime > 0 && System.currentTimeMillis() > expiryTime;
    }
}