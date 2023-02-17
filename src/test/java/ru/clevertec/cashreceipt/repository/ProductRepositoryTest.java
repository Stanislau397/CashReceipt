package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.Product;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Nested
    class SelectProductByIdTest {

        @ParameterizedTest
        @CsvSource(value = {
                "1, 2.5, Milk, true",
                "2, 10.3, Onion, false"
        })
        void checkSelectProductByIdShouldReturnProduct(Long productId, BigDecimal price, String name, Boolean promotional) {
            //given
            Product product = Product.builder()
                    .productId(productId)
                    .price(price)
                    .name(name)
                    .promotional(promotional)
                    .build();
            underTest.save(product);

            //when
            Optional<Product> expectedProduct = underTest.selectById(productId);

            //then
            assertThat(expectedProduct).isEqualTo(Optional.of(product));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1", "2"
        })
        void checkSelectProductByIdShouldBeEmpty(Long productId) {
            //when
            Optional<Product> expectedProduct = underTest.selectById(productId);
            //then
            assertThat(expectedProduct).isEmpty();
        }
    }
}