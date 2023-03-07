package ru.clevertec.cashreceipt.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public interface Cache<T> {

    void put(Long key, T value);

    Optional<T> get(Long key);

    void remove(Long key);
}
