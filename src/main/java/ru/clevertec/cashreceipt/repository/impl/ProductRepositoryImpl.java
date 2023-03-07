package ru.clevertec.cashreceipt.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;

import java.util.Optional;

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
        entityManager.createQuery("DELETE FROM Product p WHERE p.productId = ?1")
                .setParameter(1, productId)
                .executeUpdate();
    }

    @Override
    public Optional<Product> selectByName(String name) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.name = ?1", Product.class)
                .setParameter(1, name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Product> selectById(Long productId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.productId = ?1", Product.class)
                .setParameter(1, productId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Product> selectProduct(Product product) {
        return entityManager
                .createQuery("SELECT p FROM Product p where p = ?1", Product.class)
                .setParameter(1, product)
                .getResultStream()
                .findFirst();
    }
}
