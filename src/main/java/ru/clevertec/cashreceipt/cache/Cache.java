package ru.clevertec.cashreceipt.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public interface Cache<K, T> {


    void put(K key, T value);

    void remove(K key);

    Optional<T> get(K key);

}
