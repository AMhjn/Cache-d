package org.caching.Interfaces;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {

    V get(K key);
    void put(K key, V value);
    void put(K key, V value, long expiry, TimeUnit timeUnit);
    boolean remove(K key);
    void clear();
    int size();
    boolean containsKey(K key);
}
