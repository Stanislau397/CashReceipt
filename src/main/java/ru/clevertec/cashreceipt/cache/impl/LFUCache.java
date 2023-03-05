package ru.clevertec.cashreceipt.cache.impl;

import ru.clevertec.cashreceipt.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

public class LFUCache<T> implements Cache<T> {


    private int capacity;
    private int minFrequency;
    private final Map<Long, T> valuesMap;
    private final Map<Long, Integer> frequencyMap;
    private final Map<Integer, LinkedHashSet<Long>> frequencyToKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.valuesMap = new HashMap<>();
        this.frequencyToKeys = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    @Override
    public Optional<T> get(Long key) {
        //if the key doesn't exist in the cache, return empty
        if (!valuesMap.containsKey(key)) {
            return Optional.empty();
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
            frequencyToKeys.get(frequency + 1).add(key);
        }
        return Optional.of(valuesMap.get(key));
    }

    @Override
    public void put(Long key, T value) {
        //if the key exists, update key and frequency
        if (valuesMap.containsKey(key)) {
            valuesMap.put(key, value);
            get(key);
        }
        //if the cache reached capacity, then remove the least frequently used key
        if (capacity == valuesMap.size()) {
            long keyToBeRemoved = frequencyToKeys.get(minFrequency)
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
