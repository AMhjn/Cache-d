package org.caching.Caches;

import org.caching.Interfaces.Cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ForeverCache<K,V> implements Cache<K,V> {

    @Override
    public Optional get(Object key) {
        return Optional.empty();
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public boolean remove(Object key) {
        return false;
    }

    @Override
    public void put(K key, V value, long expiry, TimeUnit timeUnit) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }
}
