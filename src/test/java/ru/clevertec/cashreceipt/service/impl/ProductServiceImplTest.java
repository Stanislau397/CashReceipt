package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.PRODUCT_BY_GIVEN_ID_NOT_FOUND;

@DataJpaTest
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    private AutoCloseable autoCloseable;
    private ProductService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ProductServiceImpl(productRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class FindProductByIdTest {

        @ParameterizedTest
        @CsvSource(value = {
                "1, 2.5, Milk, true",
                "2, 10.3, Onion, false"
        })
        void checkShouldFindProductById(Long productId, BigDecimal price, String name, Boolean promotional) {
            //given
            Product expectedProduct = Product.builder()
                    .productId(productId)
                    .name(name)
                    .price(price)
                    .promotional(promotional)
                    .build();

            //when
            when(productRepository.selectById(productId))
                    .thenReturn(Optional.of(expectedProduct));
            Product actualProduct = underTest.findProductById(productId);

            //then
            assertThat(actualProduct).isEqualTo(expectedProduct);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1", "2"
        })
        void checkFindProductByIdShouldThrowEntityNotFoundException(Long productId) {
            //when
            when(productRepository.selectById(productId))
                    .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> underTest.findProductById(productId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining(String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, productId));
        }
    }

    @Nested
    class CalculatePriceForAllCashReceiptProductsTests {

        @Test
        void checkShouldCalculatePriceForAllCashReceiptProductsWhenSubtotalIsPresent() {
            //given
            BigDecimal expectedPriceForAllCashReceiptProducts = BigDecimal.valueOf(22.5);
            TotalPrice totalPrice = TotalPrice.builder()
                    .itemTotal(BigDecimal.valueOf(25))
                    .discount(BigDecimal.valueOf(2.5))
                    .subtotal(BigDecimal.valueOf(22.5))
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .totalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);

            //when
            BigDecimal actualPriceForAllCashReceiptProducts = underTest.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);

            //then
            assertThat(actualPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedPriceForAllCashReceiptProducts);
        }

        @Test
        void checkShouldCalculatePriceForAllCashReceiptProductsWithoutSubtotal() {
            //given
            BigDecimal expectedPriceForAllCashReceiptProducts = BigDecimal.valueOf(25);
            TotalPrice totalPrice = TotalPrice.builder()
                    .itemTotal(BigDecimal.valueOf(25))
                    .discount(BigDecimal.valueOf(2.5))
                    .subtotal(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .totalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);

            //when
            BigDecimal actualPriceForAllCashReceiptProducts = underTest.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);

            //then
            assertThat(actualPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedPriceForAllCashReceiptProducts);
        }
    }

    @Nested
    class BuildTotalPriceForAllCashReceiptProductsTest {

        @Test
        void checkShouldBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsEmpty() {
            //given
            TotalPrice expectedTotalPriceForAllCashReceiptProducts = TotalPrice.builder()
                    .itemTotal(BigDecimal.valueOf(100))
                    .subtotal(BigDecimal.ZERO)
                    .discount(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .totalPrice(expectedTotalPriceForAllCashReceiptProducts)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            DiscountCard discountCard = DiscountCard.builder().build();

            //when
            TotalPrice actualTotalPriceForAllCashReceiptProducts =
                    underTest.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);

            //then
            assertThat(actualTotalPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedTotalPriceForAllCashReceiptProducts);
        }

        @Test
        void checkShouldBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsPresent() {
            //given
            TotalPrice expectedTotalPriceForAllCashReceiptProducts = TotalPrice.builder()
                    .itemTotal(BigDecimal.valueOf(100))
                    .subtotal(BigDecimal.valueOf(90))
                    .discount(BigDecimal.valueOf(10))
                    .build();
            TotalPrice totalPrice = TotalPrice.builder()
                    .itemTotal(BigDecimal.valueOf(100))
                    .subtotal(BigDecimal.ZERO)
                    .discount(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .totalPrice(totalPrice)
                    .build();
            List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
            DiscountCard discountCard = DiscountCard.builder()
                    .discountCardId(1L)
                    .discountPercent(10)
                    .build();

            //when
            TotalPrice actualTotalPriceForAllCashReceiptProducts =
                    underTest.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);

            //then
            assertThat(actualTotalPriceForAllCashReceiptProducts)
                    .isEqualTo(expectedTotalPriceForAllCashReceiptProducts);
        }
    }

    @Nested
    class BuildCashReceiptProductsTest {

        @Test
        void checkShouldBuildCashReceiptProductsWithoutDiscountByProductIdAndQuantity() {
            //given
            Long productId = 1L;
            Integer quantity = 1;
            BigDecimal price = BigDecimal.valueOf(1);
            Map<Long, Integer> productIdAndQuantityMap = Map.of(productId, quantity);
            Product product = Product.builder()
                    .productId(productId)
                    .price(price)
                    .promotional(false)
                    .build();
            TotalPrice totalPrice = TotalPrice.builder()
                    .itemTotal(price)
                    .discount(BigDecimal.ZERO)
                    .subtotal(BigDecimal.ZERO)
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .product(product)
                    .totalPrice(totalPrice)
                    .quantity(quantity)
                    .build();
            List<CashReceiptProduct> expectedProducts = List.of(cashReceiptProduct);

            //when
            when(productRepository.selectById(productId)).thenReturn(Optional.of(product));
            List<CashReceiptProduct> actualProducts = underTest
                    .buildCashReceiptProductsByProductIdAndQuantity(productIdAndQuantityMap);

            //then
            assertThat(actualProducts).isEqualTo(expectedProducts);
        }

        @Test
        void checkShouldBuildCashReceiptProductsWithDiscountByProductIdAndQuantity() {
            //given
            Long productId = 1L;
            Integer quantity = 6;
            BigDecimal price = BigDecimal.valueOf(10);
            Map<Long, Integer> productIdAndQuantityMap = Map.of(productId, quantity);
            Product product = Product.builder()
                    .productId(productId)
                    .price(price)
                    .promotional(true)
                    .build();
            TotalPrice totalPrice = TotalPrice.builder()
                    .itemTotal(price.multiply(BigDecimal.valueOf(quantity)))
                    .discount(BigDecimal.valueOf(6))
                    .subtotal(BigDecimal.valueOf(54))
                    .build();
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .product(product)
                    .totalPrice(totalPrice)
                    .quantity(quantity)
                    .build();
            List<CashReceiptProduct> expectedProducts = List.of(cashReceiptProduct);

            //when
            when(productRepository.selectById(productId)).thenReturn(Optional.of(product));
            List<CashReceiptProduct> actualProducts = underTest
                    .buildCashReceiptProductsByProductIdAndQuantity(productIdAndQuantityMap);

            //then
            assertThat(actualProducts).isEqualTo(expectedProducts);
        }
    }

    @Test
    void checkShouldCalculateAllProductsPriceWithDiscount() {
        //given
        BigDecimal expectedProductsPrice = BigDecimal.valueOf(4.6);
        BigDecimal totalPriceForAllProducts = BigDecimal.valueOf(20);
        BigDecimal discountForAllProducts = BigDecimal.valueOf(15.4);

        //when
        BigDecimal actualProductsPrice =
                underTest.calculateAllProductsPriceWithDiscount(totalPriceForAllProducts, discountForAllProducts);

        //then
        assertThat(actualProductsPrice).isEqualTo(expectedProductsPrice);
    }

    @Test
    void checkShouldCalculateDiscountForAllProducts() {
        //given
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        DiscountCard discountCard = DiscountCard.builder()
                .discountPercent(5)
                .discountCardId(1L)
                .build();
        BigDecimal expectedDiscount = BigDecimal.valueOf(1);

        //when
        BigDecimal actualDiscount = underTest.calculateDiscountForAllProducts(productsTotalPrice, discountCard);

        //then
        assertThat(actualDiscount).isEqualTo(expectedDiscount);
    }


    @Test
    void checkShouldCalculateDiscountForSingleProduct() {
        //given
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        Integer discount = 5;
        BigDecimal expectedDiscount = BigDecimal.valueOf(1);

        //when
        BigDecimal actualDiscount = underTest.calculateDiscountForSingleProduct(productsTotalPrice, discount);

        //then
        assertThat(actualDiscount).isEqualTo(expectedDiscount);
    }

    @Test
    void checkShouldCalculateProductPriceWithDiscount() {
        //given
        BigDecimal totalPrice = BigDecimal.valueOf(10);
        BigDecimal discountAmount = BigDecimal.valueOf(5);
        BigDecimal expectedProductPrice = BigDecimal.valueOf(5);

        //when
        BigDecimal actualProductPrice = underTest.calculateProductPriceWithDiscount(totalPrice, discountAmount);

        //then
        assertThat(actualProductPrice).isEqualTo(expectedProductPrice);
    }

    @Test
    void checkShouldCalculateProductPriceBasedOnQuantity() {
        //given
        Integer quantity = 5;
        BigDecimal productPrice = BigDecimal.valueOf(5);
        BigDecimal expectedProductPriceBasedOnQuantity = BigDecimal.valueOf(25);

        //when
        BigDecimal actualProductPriceBasedOnQuantity = underTest.calculateProductPriceBasedOnQuantity(quantity, productPrice);

        //then
        assertThat(actualProductPriceBasedOnQuantity)
                .isEqualTo(expectedProductPriceBasedOnQuantity);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "25, 2.5, 22.5",
            "30, 25, 5"
    })
    void checkShouldBuildTotalPriceForSingleCashReceiptProduct(BigDecimal itemTotal, BigDecimal discount, BigDecimal subtotal) {
        //given
        TotalPrice expectedTotalPrice = TotalPrice.builder()
                .itemTotal(itemTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();

        //when
        TotalPrice actualTotalPrice = underTest.buildTotalPriceForSingleCashReceiptProduct(itemTotal, discount, subtotal);

        //then
        assertThat(actualTotalPrice).isEqualTo(expectedTotalPrice);
    }
}