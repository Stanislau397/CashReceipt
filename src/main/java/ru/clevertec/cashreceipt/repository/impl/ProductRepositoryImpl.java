package ru.clevertec.cashreceipt.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product save(Product product) {
        return entityManager.merge(product);
    }

    @Override
    public Product update(Product product) {
        return entityManager.merge(product);
    }

    @Override
    public Product delete(Product product) {
        entityManager.remove(product);
        return product;
    }

    @Override
    public Optional<Product> selectById(Long productId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.productId = ?1", Product.class)
                .setParameter(1, productId)
                .getResultStream()
                .findFirst();
    }
}
