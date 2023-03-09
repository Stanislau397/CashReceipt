package ru.clevertec.cashreceipt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cashreceipt.entity.Product;

@Repository
public interface ProductRepository {

    @Transactional
    Product save(Product product);

    @Transactional
    Product update(Product product);

    @Transactional
    void deleteById(Long productId);

    Product selectByName(String name);

    Product selectById(Long productId);

    Product selectProduct(Product product);
}
