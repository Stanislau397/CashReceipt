package ru.clevertec.cashreceipt.repository.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

class ProxyProductRepositoryTest {

    private ProxyProductRepository proxyProductRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepositoryImpl.class);
        proxyProductRepository = new ProxyProductRepository(productRepository);
    }

    @Test
    void checkSaveProductShouldReturnProduct() {
        Product expectedProduct = ProductTestBuilder.aProduct().build();

        doReturn(expectedProduct)
                .when(productRepository)
                .save(expectedProduct);

        Product actualProduct = proxyProductRepository.save(expectedProduct);

        assertThat(actualProduct).isEqualTo(expectedProduct);

    }

    @Test
    void checkUpdateProductShouldReturnNewProduct() {
        Product product = ProductTestBuilder.aProduct().build();

        doReturn(product)
                .when(productRepository)
                .update(product);

        Product actualProduct = proxyProductRepository.update(product);
        System.out.println(actualProduct);

        assertThat(actualProduct).isEqualTo(product);
    }

    @Test
    void checkDeleteProductShouldBeNull() {
        Product product = ProductTestBuilder.aProduct().build();
        Long productId = product.getProductId();

        doNothing()
                .when(productRepository)
                .deleteById(productId);

        proxyProductRepository.deleteById(productId);

        Product actualProduct = proxyProductRepository.selectById(productId);

        assertThat(actualProduct).isNull();
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

            doReturn(expectedProduct)
                    .when(productRepository)
                    .save(expectedProduct);

            Product product = proxyProductRepository.save(expectedProduct);

            doReturn(product)
                    .when(productRepository)
                    .selectById(product.getProductId());

            Product actualProduct = proxyProductRepository.selectById(product.getProductId());

            assertThat(actualProduct).isEqualTo(expectedProduct);
        }

        @ParameterizedTest
        @MethodSource("productIdProviderFactory")
        void checkSelectProductByIdShouldBeEmpty(Long productId) {
            doReturn(null)
                    .when(productRepository)
                    .selectById(productId);

            Product actualProduct = proxyProductRepository.selectById(productId);

            assertThat(actualProduct).isNull();
        }
    }

    @Test
    void checkShouldSelectProductByName() {
        Product product = ProductTestBuilder.aProduct().build();
        String productName = product.getName();

        doReturn(product)
                .when(productRepository)
                .selectByName(productName);

        Product actualProduct = proxyProductRepository.selectByName(productName);

        assertThat(actualProduct).isNotNull();
    }

    @Test
    void checkShouldSelectProduct() {
        Product product = ProductTestBuilder.aProduct().build();

        doReturn(product)
                .when(productRepository)
                .selectProduct(product);

        Product actualProduct = proxyProductRepository.selectProduct(product);

        assertThat(actualProduct).isNotNull();
    }
}