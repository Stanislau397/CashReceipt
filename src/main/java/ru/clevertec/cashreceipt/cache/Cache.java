package ru.clevertec.cashreceipt.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public interface Cache<K, V> {


    void put(K key, V value);

    void remove(K key);

    Optional<V> get(K key);

}
