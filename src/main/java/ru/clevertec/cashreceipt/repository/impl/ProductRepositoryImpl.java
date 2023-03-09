package ru.clevertec.cashreceipt.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;

import java.util.Optional;

import static ru.clevertec.cashreceipt.repository.HqlQuery.DELETE_PRODUCT_BY_ID;
import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_PRODUCT;
import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_PRODUCT_BY_ID;
import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_PRODUCT_BY_NAME;

@Repository
@NoArgsConstructor
@AllArgsConstructor
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
    public void deleteById(Long productId) {
        entityManager.createQuery(DELETE_PRODUCT_BY_ID)
                .setParameter(1, productId)
                .executeUpdate();
    }

    @Override
    public Product selectByName(String name) {
        return entityManager
                .createQuery(SELECT_PRODUCT_BY_NAME, Product.class)
                .setParameter(1, name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product selectById(Long productId) {
        return entityManager
                .createQuery(SELECT_PRODUCT_BY_ID, Product.class)
                .setParameter(1, productId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product selectProduct(Product product) {
        return entityManager
                .createQuery(SELECT_PRODUCT, Product.class)
                .setParameter(1, product)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
