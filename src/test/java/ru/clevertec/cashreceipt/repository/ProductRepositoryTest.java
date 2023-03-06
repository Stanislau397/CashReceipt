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
import org.springframework.test.annotation.DirtiesContext;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(entityManager);
    }

    @AfterEach
    void tearDown() {
        productRepository = null;
    }

    @Test
    void checkSaveProductShouldReturnProduct() {
        Product expectedProduct = ProductTestBuilder.aProduct().build();

        Product actualProduct = productRepository.save(expectedProduct);

        assertThat(actualProduct).isEqualTo(expectedProduct);

    }

    @Test
    void checkUpdateProductShouldReturnNewProduct() {
        Product product = ProductTestBuilder.aProduct().build();

        productRepository.save(product);

        Product updatedProduct = ProductTestBuilder.aProduct()
                .withPromotional(true)
                .withPrice(BigDecimal.valueOf(123.2))
                .build();

        Product actualProduct = productRepository.update(updatedProduct);
        System.out.println(actualProduct);

        assertThat(actualProduct).isNotEqualTo(product);
    }

    @Test
    void checkDeleteProductShouldBeEmpty() {
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

        @Test
        void checkSelectProductByIdShouldReturnProduct() {
            Product expectedProduct = ProductTestBuilder
                    .aProduct()
                    .build();
            Product product = productRepository.save(expectedProduct);

            Optional<Product> actualProduct = productRepository.selectById(product.getProductId());

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