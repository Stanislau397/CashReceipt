package ru.clevertec.cashreceipt.factory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.clevertec.cashreceipt.repository.ProductRepository;
import ru.clevertec.cashreceipt.repository.handler.ProductRepositoryHandler;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;

import java.lang.reflect.Proxy;

@Component
public class ProductRepositoryFactory {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public ProductRepository createProductRepository() {
        ProductRepository productRepository = new ProductRepositoryImpl(entityManager);
        ClassLoader productRepositoryClassLoader = productRepository.getClass().getClassLoader();
        Class<?>[] productRepositoryInterfaces = productRepository.getClass().getInterfaces();
        return (ProductRepository) Proxy.newProxyInstance(productRepositoryClassLoader, productRepositoryInterfaces,
                new ProductRepositoryHandler(productRepository));
    }
}
