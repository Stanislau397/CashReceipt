package ru.clevertec.cashreceipt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cashreceipt.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository {

    @Transactional
    Product save(Product product);

    @Transactional
    Product update(Product product);

    @Transactional
    void deleteById(Long productId);

    Optional<Product> selectByName(String name);

    Optional<Product> selectById(Long productId);

    Optional<Product> selectProduct(Product product);
}
