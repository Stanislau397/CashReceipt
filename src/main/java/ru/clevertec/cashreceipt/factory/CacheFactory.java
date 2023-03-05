package ru.clevertec.cashreceipt.factory;

import org.springframework.beans.factory.annotation.Value;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.impl.LFUCache;
import ru.clevertec.cashreceipt.cache.impl.LRUCache;

public class CacheFactory<T> {

    @Value("${cache.capacity}")
    private Integer capacity;
    @Value(("${cache.algorithm}"))
    private String cacheAlgorithm;

    Cache<T> create() {
        Cache<T> cache = new LFUCache<>(capacity);
        if ("LRUCache".equals(cacheAlgorithm)) {
            cache = new LRUCache<>(capacity);
        }
        return cache;
    }
}
