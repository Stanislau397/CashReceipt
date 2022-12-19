package ru.clevertec.cashier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashier.entity.Product;

import java.util.Optional;

import static ru.clevertec.cashier.repository.HqlQuery.SELECT_PRODUCT_BY_ID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(SELECT_PRODUCT_BY_ID)
    Optional<Product> selectById(Long productId);
}
