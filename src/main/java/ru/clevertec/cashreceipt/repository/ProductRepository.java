package ru.clevertec.cashreceipt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;

import java.util.Optional;

import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_PRODUCT_BY_ID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(SELECT_PRODUCT_BY_ID)
    Optional<Product> selectById(Long productId);
}
