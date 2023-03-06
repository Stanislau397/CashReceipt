package ru.clevertec.cashreceipt.factory;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.impl.LFUCache;
import ru.clevertec.cashreceipt.cache.impl.LRUCache;

public class CustomCashFactory<T> {

    private final int capacity = 100;
    private final String cacheAlgorithm = "LFUCache";

    public Cache<T> create() {
        System.out.println(cacheAlgorithm);
        System.out.println(capacity);
        Cache<T> cache = new LFUCache<>(capacity);
        if ("LRUCache".equals(cacheAlgorithm)) {
            cache = new LRUCache<>(capacity);
        }
        return cache;
    }
}
