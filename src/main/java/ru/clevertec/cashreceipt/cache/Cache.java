package ru.clevertec.cashreceipt.cache;

import java.util.Optional;

public interface Cache<K, T> {

    void put(K key, T value);

    Optional<T> get(K key);
}
