package org.caching.Caches;

import org.caching.CacheEntry;
import org.caching.Interfaces.Cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K,V> implements Cache<K,V> {

    private final Map<K, CacheEntry<V>> cache;
    private final int capacity;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // Using LinkedHashMap with access-order to implement LRU
        this.cache = new LinkedHashMap<K, CacheEntry<V>>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public V get(K key) {
        try {
            lock.readLock().lock();
            CacheEntry<V> entry = cache.get(key);

            if (entry == null) {
                return null;
            }

            if (entry.isExpired()) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    // Double-check expiration
                    entry = cache.get(key);
                    if (entry != null && entry.isExpired()) {
                        cache.remove(key);
                        return null;
                    }
                } finally {
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }

            return entry.getValue();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        put(key, value, 0, TimeUnit.MILLISECONDS); // No expiration
    }

    @Override
    public void put(K key, V value, long expiry, TimeUnit timeUnit) {
        try {
            lock.writeLock().lock();

            long expiryTimeMillis = 0; // 0 means no expiration
            if (expiry > 0) {
                expiryTimeMillis = System.currentTimeMillis() + timeUnit.toMillis(expiry);
            }

            cache.put(key, new CacheEntry<>(value, expiryTimeMillis));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(K key) {
        try {
            lock.writeLock().lock();
            CacheEntry<V> removed = cache.remove(key);
            if (removed != null) {
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.writeLock().lock();
            int sizeBefore = cache.size();
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.readLock().lock();
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(K key) {
        try {
            lock.readLock().lock();
            CacheEntry<V> entry = cache.get(key);
            if (entry == null) {
                return false;
            }

            if (entry.isExpired()) {
                // Release read lock and acquire write lock
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    // Double-check the key and expiration
                    entry = cache.get(key);
                    if (entry != null && entry.isExpired()) {
                        cache.remove(key);
                        return false;
                    }
                    return entry != null;
                } finally {
                    // Downgrade to read lock
                    lock.readLock().lock();
                    lock.writeLock().unlock();
                }
            }

            return true;
        } finally {
            lock.readLock().unlock();
        }
    }
}
