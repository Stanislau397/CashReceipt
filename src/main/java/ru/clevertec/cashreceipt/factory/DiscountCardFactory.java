package ru.clevertec.cashreceipt.factory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;
import ru.clevertec.cashreceipt.repository.handler.DiscountRepositoryHandler;
import ru.clevertec.cashreceipt.repository.impl.DiscountCardRepositoryImpl;

import java.lang.reflect.Proxy;

@Component
public class DiscountCardFactory {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public DiscountCardRepository createDiscountCardRepository() {
        DiscountCardRepository discountCardRepository = new DiscountCardRepositoryImpl(entityManager);
        ClassLoader productRepositoryClassLoader = discountCardRepository.getClass().getClassLoader();
        Class<?>[] productRepositoryInterfaces = discountCardRepository.getClass().getInterfaces();
        return (DiscountCardRepository) Proxy.newProxyInstance(productRepositoryClassLoader, productRepositoryInterfaces,
                new DiscountRepositoryHandler(discountCardRepository));
    }
}
