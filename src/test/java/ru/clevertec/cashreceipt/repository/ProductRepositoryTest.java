package ru.clevertec.cashreceipt.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(entityManager);
    }

    @AfterEach
    void tearDown() {
        productRepository = null;
    }

    @Test
    void update() {
        Product product = ProductTestBuilder.aProduct().build();

        productRepository.save(product);

        Product updatedProduct = ProductTestBuilder.aProduct()
                .withPromotional(true)
                .withPrice(BigDecimal.valueOf(123.2))
                .build();

        Product actualProduct = productRepository.update(updatedProduct);

        assertThat(actualProduct).isNotEqualTo(product);
    }

    @Test
    void delete() {
        Product product = ProductTestBuilder.aProduct().build();

        Product savedProduct = productRepository.save(product);
        productRepository.delete(savedProduct);

        Optional<Product> actualProduct = productRepository.selectById(savedProduct.getProductId());

        assertThat(actualProduct).isEmpty();
    }

    @Nested
    class SelectProductByIdTest {

        static LongStream productIdProviderFactory() {
            return LongStream.of(1, 2, 3);
        }

        @ParameterizedTest
        @MethodSource("productIdProviderFactory")
        void checkSelectProductByIdShouldReturnProduct(Long productId) {
            Product expectedProduct = ProductTestBuilder
                    .aProduct()
                    .withProductId(productId)
                    .build();
            productRepository.save(expectedProduct);

            Optional<Product> actualProduct = productRepository.selectById(productId);

            assertThat(actualProduct).isEqualTo(Optional.of(expectedProduct));
        }

        @ParameterizedTest
        @MethodSource("productIdProviderFactory")
        void checkSelectProductByIdShouldBeEmpty(Long productId) {
            Optional<Product> actualProduct = productRepository.selectById(productId);

            assertThat(actualProduct).isEmpty();
        }
    }
}