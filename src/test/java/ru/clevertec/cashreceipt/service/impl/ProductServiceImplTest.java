package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.repository.ProductRepository;
import ru.clevertec.cashreceipt.service.ProductService;
import ru.clevertec.cashreceipt.util.testbuilder.impl.CashReceiptProductTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.ProductTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.impl.TotalPriceTestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.PRODUCT_BY_GIVEN_ID_NOT_FOUND;

@DataJpaTest
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    private AutoCloseable autoCloseable;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class FindProductByIdTest {

        static LongStream productIdArgumentsProvider() {
            return LongStream.of(1, 23, 55);
        }

        @ParameterizedTest
        @MethodSource("productIdArgumentsProvider")
        void checkFindProductByIdShouldReturnProduct(Long productId) {
            Product expectedProduct = ProductTestBuilder
                    .aProduct()
                    .withProductId(productId)
                    .build();

            doReturn(Optional.of(expectedProduct))
                    .when(productRepository).selectById(productId);

            Product actualProduct = productService.findProductById(productId);

            assertThat(actualProduct).isEqualTo(expectedProduct);
        }

        @ParameterizedTest
        @MethodSource("productIdArgumentsProvider")
        void checkFindProductByIdShouldThrowEntityNotFoundException(Long productId) {
            assertThatThrownBy(() -> productService.findProductById(productId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining(String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, productId));
        }
    }

    @Nested
    class CalculatePriceForAllCashReceiptProductsTests {

        @Test
        void checkShouldCalculatePriceForAllCashReceiptProductsWhenSubtotalIsPresent() {
            TotalPrice totalPrice = TotalPriceTestBuilder
                    .aTotalPrice()
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder
                    .aCashReceiptProduct()
                    .withTotalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            BigDecimal expectedPriceForAllCashReceiptProducts = BigDecimal.valueOf(90);

            BigDecimal actualPriceForAllCashReceiptProducts =
                    productService.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);

            assertThat(actualPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedPriceForAllCashReceiptProducts);
        }

        @Test
        void checkShouldCalculatePriceForAllCashReceiptProductsWithoutSubtotal() {
            TotalPrice totalPrice = TotalPriceTestBuilder
                    .aTotalPrice()
                    .withSubtotal(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder
                    .aCashReceiptProduct()
                    .withTotalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            BigDecimal expectedPriceForAllCashReceiptProducts = BigDecimal.valueOf(100);

            BigDecimal actualPriceForAllCashReceiptProducts =
                    productService.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);

            assertThat(actualPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedPriceForAllCashReceiptProducts);
        }
    }

    @Nested
    class BuildTotalPriceForAllCashReceiptProductsTest {

        @Test
        void checkShouldBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsEmpty() {
            TotalPrice expectedTotalPriceForAllCashReceiptProducts = TotalPriceTestBuilder
                    .aTotalPrice()
                    .withSubtotal(BigDecimal.ZERO)
                    .withDiscount(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder
                    .aCashReceiptProduct()
                    .withTotalPrice(expectedTotalPriceForAllCashReceiptProducts)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            DiscountCard discountCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountCardId(null)
                    .build();

            TotalPrice actualTotalPriceForAllCashReceiptProducts =
                    productService.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);

            assertThat(actualTotalPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedTotalPriceForAllCashReceiptProducts);
        }

        @Test
        void checkShouldBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsPresent() {
            TotalPrice totalPrice = TotalPriceTestBuilder
                    .aTotalPrice()
                    .withDiscount(BigDecimal.ZERO)
                    .withSubtotal(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder
                    .aCashReceiptProduct()
                    .withTotalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            DiscountCard discountCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountPercent(10)
                    .build();

            TotalPrice expectedTotalPriceForAllCashReceiptProducts = TotalPriceTestBuilder
                    .aTotalPrice()
                    .build();

            TotalPrice actualTotalPriceForAllCashReceiptProducts =
                    productService.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);

            assertThat(actualTotalPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedTotalPriceForAllCashReceiptProducts);
        }
    }

    @Nested
    class BuildCashReceiptProductsTest {

        private static Stream<Arguments> productIdAndQuantityLessThan5ArgumentsProvider() {
            return Stream.of(
                    Arguments.of(1L, 1),
                    Arguments.of(1L, 2),
                    Arguments.of(1L, 3),
                    Arguments.of(1L, 4),
                    Arguments.of(1L, 5)
            );
        }

        private static Stream<Arguments> productIdAndQuantityGreaterThan5ArgumentsProvider() {
            return Stream.of(
                    Arguments.of(1L, 6),
                    Arguments.of(1L, 7),
                    Arguments.of(1L, 8),
                    Arguments.of(1L, 9),
                    Arguments.of(1L, 10)
            );
        }

        @ParameterizedTest
        @MethodSource("productIdAndQuantityLessThan5ArgumentsProvider")
        void checkShouldBuildCashReceiptProductsWithoutDiscountByProductIdAndQuantity(Long productId, Integer quantity) {
            Map<Long, Integer> productIdAndQuantityMap = Map.of(productId, quantity);

            Product product = ProductTestBuilder.aProduct().build();

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            TotalPrice totalPrice = TotalPriceTestBuilder.aTotalPrice()
                    .withItemTotal(itemTotal)
                    .withDiscount(BigDecimal.ZERO)
                    .withSubtotal(BigDecimal.ZERO)
                    .build();

            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct()
                    .withTotalPrice(totalPrice)
                    .withQuantity(quantity)
                    .build();

            List<CashReceiptProduct> expectedProducts = List.of(cashReceiptProduct);

            doReturn(Optional.of(product))
                    .when(productRepository).selectById(productId);

            List<CashReceiptProduct> actualProducts = productService
                    .buildCashReceiptProductsByProductIdAndQuantity(productIdAndQuantityMap);

            assertThat(actualProducts).isEqualTo(expectedProducts);
        }

        @ParameterizedTest
        @MethodSource("productIdAndQuantityGreaterThan5ArgumentsProvider")
        void checkShouldBuildCashReceiptProductsWithDiscountByProductIdAndQuantity(Long productId, Integer quantity) {
            Map<Long, Integer> productIdAndQuantityMap = Map.of(productId, quantity);

            Product product = ProductTestBuilder.aProduct()
                    .withPrice(BigDecimal.valueOf(10))
                    .withPromotional(true)
                    .build();

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            BigDecimal subtotal = itemTotal.subtract(BigDecimal.valueOf(quantity));

            TotalPrice totalPrice = TotalPriceTestBuilder.aTotalPrice()
                    .withItemTotal(itemTotal)
                    .withSubtotal(subtotal)
                    .withDiscount(BigDecimal.valueOf(quantity))
                    .build();

            CashReceiptProduct cashReceiptProduct = CashReceiptProductTestBuilder.aCashReceiptProduct()
                    .withProduct(product)
                    .withQuantity(quantity)
                    .withTotalPrice(totalPrice)
                    .build();

            List<CashReceiptProduct> expectedProducts = List.of(cashReceiptProduct);

            doReturn(Optional.of(product))
                    .when(productRepository).selectById(productId);

            List<CashReceiptProduct> actualProducts = productService
                    .buildCashReceiptProductsByProductIdAndQuantity(productIdAndQuantityMap);

            assertThat(actualProducts).isEqualTo(expectedProducts);
        }
    }

    @Test
    void checkShouldCalculateAllProductsPriceWithDiscount() {
        BigDecimal totalPriceForAllProducts = BigDecimal.valueOf(20);
        BigDecimal discountForAllProducts = BigDecimal.valueOf(15.4);
        BigDecimal expectedProductsPrice = BigDecimal.valueOf(4.6);

        BigDecimal actualProductsPrice =
                productService.calculateAllProductsPriceWithDiscount(totalPriceForAllProducts, discountForAllProducts);

        assertThat(actualProductsPrice).isEqualTo(expectedProductsPrice);
    }

    @Test
    void checkShouldCalculateDiscountForAllProducts() {
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
        BigDecimal expectedDiscount = BigDecimal.valueOf(1);

        BigDecimal actualDiscount = productService.calculateDiscountForAllProducts(productsTotalPrice, discountCard);

        assertThat(actualDiscount).isEqualTo(expectedDiscount);
    }


    @Test
    void checkShouldCalculateDiscountForSingleProduct() {
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        Integer discount = 5;
        BigDecimal expectedDiscount = BigDecimal.valueOf(1);

        BigDecimal actualDiscount = productService.calculateDiscountForSingleProduct(productsTotalPrice, discount);

        assertThat(actualDiscount).isEqualTo(expectedDiscount);
    }

    @Test
    void checkShouldCalculateProductPriceWithDiscount() {
        BigDecimal totalPrice = BigDecimal.valueOf(10);
        BigDecimal discountAmount = BigDecimal.valueOf(5);
        BigDecimal expectedProductPrice = BigDecimal.valueOf(5);

        BigDecimal actualProductPrice = productService.calculateProductPriceWithDiscount(totalPrice, discountAmount);

        assertThat(actualProductPrice).isEqualTo(expectedProductPrice);
    }

    @Test
    void checkShouldCalculateProductPriceBasedOnQuantity() {
        Integer quantity = 5;
        BigDecimal productPrice = BigDecimal.valueOf(5);
        BigDecimal expectedProductPriceBasedOnQuantity = BigDecimal.valueOf(25);

        BigDecimal actualProductPriceBasedOnQuantity = productService.calculateProductPriceBasedOnQuantity(quantity, productPrice);

        assertThat(actualProductPriceBasedOnQuantity)
                .isEqualTo(expectedProductPriceBasedOnQuantity);
    }

    private static Stream<Arguments> totalPriceForSingleCashReceiptProductArgumentsProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(25), BigDecimal.valueOf(2.5), BigDecimal.valueOf(22.5)),
                Arguments.of(BigDecimal.valueOf(30), BigDecimal.valueOf(25), BigDecimal.valueOf(5))
        );
    }

    @ParameterizedTest
    @MethodSource("totalPriceForSingleCashReceiptProductArgumentsProvider")
    void checkShouldBuildTotalPriceForSingleCashReceiptProduct(BigDecimal itemTotal, BigDecimal discount, BigDecimal subtotal) {
        TotalPrice expectedTotalPrice = TotalPrice.builder()
                .itemTotal(itemTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();

        TotalPrice actualTotalPrice = productService.buildTotalPriceForSingleCashReceiptProduct(itemTotal, discount, subtotal);

        assertThat(actualTotalPrice).isEqualTo(expectedTotalPrice);
    }
}