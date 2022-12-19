package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.Product;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void willSelectProductById() {
        //given
        Long productId = 1L;
        Product product = Product.builder()
                .productId(productId)
                .price(BigDecimal.valueOf(2.5))
                .name("Milk")
                .promotional(true)
                .build();
        productRepository.save(product);
        //when
        Optional<Product> expectedProduct = productRepository.selectById(productId);
        //then
        assertThat(expectedProduct).isEqualTo(Optional.of(product));
    }

    @Test
    void willNotSelectProductById() {
        //given
        Long productId = 2L;
        //when
        Optional<Product> expectedProduct = productRepository.selectById(productId);
        //then
        assertThat(expectedProduct).isEqualTo(Optional.empty());
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }
}