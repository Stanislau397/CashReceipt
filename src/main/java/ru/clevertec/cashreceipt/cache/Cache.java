package ru.clevertec.cashreceipt.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

<<<<<<< HEAD
@Component
@Scope("prototype")
public interface Cache<T> {
=======
public interface Cache<K, T> {
>>>>>>> feature/cache

    void put(K key, T value);

<<<<<<< HEAD
    Optional<T> get(Long key);

    void remove(Long key);
=======
    Optional<T> get(K key);
>>>>>>> feature/cache
}
