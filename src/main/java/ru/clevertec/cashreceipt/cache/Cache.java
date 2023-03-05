package ru.clevertec.cashreceipt.cache;

import java.util.Optional;

public interface Cache<T> {

    void put(Long key, T value);

    Optional<T> get(Long key);
}
