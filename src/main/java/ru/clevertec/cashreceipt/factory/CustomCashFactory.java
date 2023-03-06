package ru.clevertec.cashreceipt.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.impl.LFUCache;
import ru.clevertec.cashreceipt.cache.impl.LRUCache;

@Component
@Scope("prototype")
public class CustomCashFactory<T> {

    @Value("${cache.capacity}")
    private int capacity;
    @Value("${cache.algorithm}")
    private String cacheAlgorithm;

    @Bean
    public Cache<T> create() {
        Cache<T> cache = new LFUCache<>(capacity);
        if ("LRUCache".equals(cacheAlgorithm)) {
            cache = new LRUCache<>(capacity);
        }
        return cache;
    }
}
