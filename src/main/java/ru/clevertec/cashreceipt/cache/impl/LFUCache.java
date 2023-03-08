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
    private final Map<K, Integer> keyFrequencyMap;
    private final Map<Integer, LinkedHashSet<K>> frequencyMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.valuesMap = new HashMap<>();
        this.frequencyMap = new HashMap<>();
        this.keyFrequencyMap = new HashMap<>();
    }

    /**
     * Gets one element from LFUCache
     * If provided key doesn't exist in LFUCache then return empty optional.
     * If provided key exists in LFUCache then increment its frequency
     */
    @Override
    public Optional<T> get(K key) {
        Optional<T> value = Optional.empty();
        if (!valuesMap.containsKey(key)) {
            return value;
        }

        int frequency = keyFrequencyMap.get(key);
        keyFrequencyMap.put(key, frequency + 1);
        frequencyMap.get(frequency)
                .remove(key);

        if (frequency == minFrequency && frequencyMap.get(frequency).isEmpty()) {
            minFrequency = minFrequency + 1;
        }
        if (!frequencyMap.containsKey(frequency + 1)) {
            frequencyMap.put(frequency + 1, new LinkedHashSet<>());
        }
        frequencyMap.get(frequency + 1).add(key);

        value = Optional.of(valuesMap.get(key));
        return value;
    }

    /**
     * Removes an Element from LFUCache
     */
    @Override
    public void remove(K key) {
        if (valuesMap.containsKey(key)) {
            valuesMap.remove(key);
            int frequency = keyFrequencyMap.get(key);
            keyFrequencyMap.remove(key);
            frequencyMap.get(frequency)
                    .remove(key);
        }
    }

    /**
     * Adds one element to LFUCache with the frequency of 1
     * If provided key exists in LFUCache, then update the value and frequency
     * If LFUCache reached capacity, then remove the least frequently used key
     */
    @Override
    public void put(K key, T value) {
        if (valuesMap.containsKey(key)) {
            valuesMap.put(key, value);
            get(key);
        }
        if (capacity == valuesMap.size()) {
            var keyToBeRemoved = frequencyMap.get(minFrequency)
                    .iterator()
                    .next();
            frequencyMap.get(minFrequency)
                    .remove(keyToBeRemoved);
            valuesMap.remove(keyToBeRemoved);
            keyFrequencyMap.remove(keyToBeRemoved);
        }

        valuesMap.put(key, value);
        minFrequency = 1;
        keyFrequencyMap.put(key, minFrequency);
        if (!frequencyMap.containsKey(minFrequency)) {
            frequencyMap.put(minFrequency, new LinkedHashSet<>());
            frequencyMap.get(minFrequency).add(key);
        }
    }
}
