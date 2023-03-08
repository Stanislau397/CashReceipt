package ru.clevertec.cashreceipt.cache.impl;

import lombok.Getter;
import ru.clevertec.cashreceipt.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

@Getter
public class LFUCache<K, T> implements Cache<K, T> {

    private final int capacity;
    private int minFrequency;
    private final Map<K, T> valuesMap;
    private final Map<K, Integer> frequencyMap;
    private final Map<Integer, LinkedHashSet<K>> frequencyToKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.valuesMap = new HashMap<>();
        this.frequencyToKeys = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    @Override
    public Optional<T> get(K key) {
        //if the key doesn't exist in the cache, return empty
        Optional<T> value = Optional.empty();
        if (!valuesMap.containsKey(key)) {
            return value;
        }
        //if the current key is used, increment the frequency of the key
        int frequency = frequencyMap.get(key);
        frequencyMap.put(key, frequency + 1);
        //update frequencyToKeys
        frequencyToKeys.get(frequency)
                .remove(key);

        if (frequency == minFrequency && frequencyToKeys.get(frequency).isEmpty()) {
            minFrequency = minFrequency + 1;
        }

        if (!frequencyToKeys.containsKey(frequency + 1)) {
            frequencyToKeys.put(frequency + 1, new LinkedHashSet<>());
        }
        frequencyToKeys.get(frequency + 1).add(key);

        value = Optional.of(valuesMap.get(key));
        return value;
    }

    @Override
    public void remove(K key) {
        if (valuesMap.containsKey(key)) {
            valuesMap.remove(key);
            int frequency = frequencyMap.get(key);
            frequencyMap.remove(key);
            frequencyToKeys.get(frequency)
                    .remove(key);
        }
    }

    @Override
    public void put(K key, T value) {
        //if the key exists, update key and frequency
        if (valuesMap.containsKey(key)) {
            valuesMap.put(key, value);
            get(key);
        }
        //if the cache reached capacity, then remove the least frequently used key
        if (capacity == valuesMap.size()) {
            var keyToBeRemoved = frequencyToKeys.get(minFrequency)
                    .iterator()
                    .next();
            frequencyToKeys.get(minFrequency)
                    .remove(keyToBeRemoved);
            valuesMap.remove(keyToBeRemoved);
            frequencyMap.remove(keyToBeRemoved);
        }

        valuesMap.put(key, value);
        frequencyMap.put(key, 1);
        minFrequency = 1;
        if (!frequencyToKeys.containsKey(1)) {
            frequencyToKeys.put(1, new LinkedHashSet<>());
            frequencyToKeys.get(1).add(key);
        }
    }
}
