package ru.clevertec.cashreceipt.repository;

import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository {

    Product save(Product product);

    Product update(Product product);

    Product delete(Product product);

    Optional<Product> selectById(Long productId);
}
