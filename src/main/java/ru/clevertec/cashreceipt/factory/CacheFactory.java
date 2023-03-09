package ru.clevertec.cashreceipt.factory;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.impl.LFUCache;
import ru.clevertec.cashreceipt.cache.impl.LRUCache;
import ru.clevertec.cashreceipt.util.PropertiesLoader;

import java.util.Properties;

public class CacheFactory<K, V> {

    private static final Properties PROPERTIES = PropertiesLoader.loadProperties();
    private static final int CAPACITY = Integer.parseInt(PROPERTIES.getProperty("capacity"));
    private static final String ALGORITHM = PROPERTIES.getProperty("algorithm");

    public Cache<K, V> createCache() {
        Cache<K, V> cache = new LFUCache<>(CAPACITY);
        if ("LRUCache".equals(ALGORITHM)) {
            cache = new LRUCache<>(CAPACITY);
        }
        return cache;
    }
}
