package ru.clevertec.cashreceipt.cache.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class LFUCacheTest {

    private static final int CAPACITY = 2;
    private LFUCache<Product> lfuCache;

    @BeforeEach
    void setUp() {
        lfuCache = Mockito.spy(new LFUCache<>(CAPACITY));
    }

    @Nested
    class PutTest {

        @Test
        void checkPutShouldCaptureKeyArgument() {
            ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            doNothing().when(lfuCache).put(
                    longArgumentCaptor.capture(),
                    any(Product.class)
            );

            lfuCache.put(productId, product);

            assertThat(longArgumentCaptor.getValue())
                    .isEqualTo(productId);
        }

        @Test
        void checkPutShouldCaptureValueArgument() {
            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            doNothing().when(lfuCache).put(
                    any(Long.class),
                    productArgumentCaptor.capture()
            );

            lfuCache.put(productId, product);

            assertThat(productArgumentCaptor.getValue())
                    .isEqualTo(product);
        }

        @Test
        void checkPutShouldAddOneElementToValuesMap() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            lfuCache.put(productId, product);

            assertThat(lfuCache.getValuesMap().size())
                    .isEqualTo(1);
        }

        @Test
        void checkPutShouldAddOneElementToFrequencyMap() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            lfuCache.put(productId, product);

            assertThat(lfuCache.getFrequencyMap().size())
                    .isEqualTo(1);
        }

        @Test
        void checkPutShouldAddOneElementToFrequencyToKeysMap() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            lfuCache.put(productId, product);

            assertThat(lfuCache.getFrequencyToKeys().size())
                    .isEqualTo(1);
        }

        @Test
        void checkPutShouldIncrementMinFrequencyTo1() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            lfuCache.put(productId, product);

            assertThat(lfuCache.getMinFrequency())
                    .isEqualTo(1);
        }

        @Test
        void checkPutShouldRemoveFirstElementWhenCapacityEqualsToMapSize() {
            int expectedSize = 2;
            Product firstProduct = ProductTestBuilder.aProduct().build();
            Product secondProduct = ProductTestBuilder.aProduct()
                    .withProductId(2L)
                    .build();
            Product thirdProduct = ProductTestBuilder.aProduct()
                    .withProductId(3L)
                    .build();

            lfuCache.put(firstProduct.getProductId(), firstProduct);
            lfuCache.put(secondProduct.getProductId(), secondProduct);
            lfuCache.put(thirdProduct.getProductId(), thirdProduct);

            assertThat(lfuCache.getValuesMap().size())
                    .isEqualTo(expectedSize);
        }

        @Test
        void checkPutShouldReplaceAnElement() {
            int expectedSize = 1;
            Product firstProduct = ProductTestBuilder.aProduct().build();
            Product secondProduct = ProductTestBuilder.aProduct()
                    .withPrice(BigDecimal.valueOf(2.76))
                    .build();

            lfuCache.put(firstProduct.getProductId(), firstProduct);
            lfuCache.put(secondProduct.getProductId(), secondProduct);

            assertThat(lfuCache.getValuesMap().size())
                    .isEqualTo(expectedSize);
        }
    }

    @Nested
    class GetTest {

        @Test
        void checkGetShouldReturnEmpty() {
            Optional<Product> actualProduct = lfuCache.get(1L);

            assertThat(actualProduct).isEmpty();
        }

        @Test
        void checkGetShouldNotBeEmpty() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();
            lfuCache.put(productId, product);

            Optional<Product> actualProduct = lfuCache.get(productId);

            assertThat(actualProduct).isNotEmpty();
        }
    }

    @Nested
    class RemoveTest {

        @Test
        void checkShouldRemoveValueFromValuesMap() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();
            lfuCache.put(productId, product);

            lfuCache.remove(productId);

            assertThat(lfuCache.getValuesMap().size())
                    .isEqualTo(0);
        }

        @Test
        void checkShouldNotRemoveValueFromValuesMap() {
            Product product = ProductTestBuilder.aProduct().build();
            Long productId = product.getProductId();

            lfuCache.put(productId, product);
            lfuCache.remove(2L);

            assertThat(lfuCache.getValuesMap().size())
                    .isEqualTo(1);
        }
    }
}