package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
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