package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void willFindProductById() {
        //given
        Long productId = 1L;
        Product product = Product.builder()
                .productId(1L)
                .name("Milk")
                .price(BigDecimal.valueOf(1.25))
                .promotional(true)
                .build();
        //when
        when(productRepository.selectById(productId)).thenReturn(Optional.of(product));
        Product expectedProduct = underTest.findProductById(productId);
        //then
        assertThat(expectedProduct).isEqualTo(product);
    }

    @Test
    void findProductByIdWillThrowEntityNotFoundException() {
        //given
        Long productId = 1L;
        //when
        when(productRepository.selectById(productId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.findProductById(productId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, productId));
    }

    @Test
    void willCalculateAllProductsPriceWithDiscount() {
        //given
        BigDecimal actual = BigDecimal.valueOf(2.5);
        BigDecimal totalPriceForProducts = BigDecimal.valueOf(4);
        BigDecimal discountForAllProducts = BigDecimal.valueOf(1.5);
        //when
        BigDecimal expected = underTest.calculateAllProductsPriceWithDiscount(totalPriceForProducts, discountForAllProducts);
        //then
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void willCalculateDiscountForAllProducts() {
        //given
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        DiscountCard discountCard = DiscountCard.builder()
                .discountPercent(5)
                .discountCardId(1L)
                .build();
        BigDecimal expected = BigDecimal.valueOf(1);
        //when
        BigDecimal actual = underTest.calculateDiscountForAllProducts(productsTotalPrice, discountCard);
        //then
        assertThat(expected).isEqualTo(actual);
    }


    @Test
    void willCalculateDiscountForSingleProduct() {
        //given
        BigDecimal productsTotalPrice = BigDecimal.valueOf(10);
        Integer discount = 5;
        BigDecimal expected = BigDecimal.valueOf(1);
        //when
        BigDecimal actual = underTest.calculateDiscountForSingleProduct(productsTotalPrice, discount);
        //then
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void willCalculateProductPriceWithDiscount() {
        //given
        BigDecimal totalPrice = BigDecimal.valueOf(10);
        BigDecimal discountAmount = BigDecimal.valueOf(5);
        BigDecimal expected = BigDecimal.valueOf(5);
        //when
        BigDecimal actual = underTest.calculateProductPriceWithDiscount(totalPrice, discountAmount);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willCalculateProductPriceBasedOnQuantity() {
        //given
        Integer quantity = 5;
        BigDecimal productPrice = BigDecimal.valueOf(5);
        BigDecimal expected = BigDecimal.valueOf(25);
        //when
        BigDecimal actual = underTest.calculateProductPriceBasedOnQuantity(quantity, productPrice);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willBuildTotalPriceForSingleCashReceiptProduct() {
        //given
        BigDecimal itemTotal = BigDecimal.valueOf(25);
        BigDecimal discount = BigDecimal.valueOf(2.5);
        BigDecimal subtotal = BigDecimal.valueOf(22.5);
        TotalPrice expected = TotalPrice.builder()
                .itemTotal(itemTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();
        //when
        TotalPrice actual = underTest.buildTotalPriceForSingleCashReceiptProduct(itemTotal, discount, subtotal);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willCalculatePriceForAllCashReceiptProductsWhenSubtotalIsPresent() {
        //given
        BigDecimal expected = BigDecimal.valueOf(22.5);
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
        BigDecimal actual = underTest.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willCalculatePriceForAllCashReceiptProductsWithoutSubtotal() {
        //given
        BigDecimal expected = BigDecimal.valueOf(25);
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
        BigDecimal actual = underTest.calculatePriceForAllCashReceiptProducts(cashReceiptProducts);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsEmpty() {
        //given
        TotalPrice expected = TotalPrice.builder()
                .itemTotal(BigDecimal.valueOf(100))
                .subtotal(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .build();
        CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                .totalPrice(expected)
                .build();
        List<CashReceiptProduct> cashReceiptProducts = List.of(cashReceiptProduct);
        DiscountCard discountCard = DiscountCard.builder().build();
        //when
        TotalPrice actual = underTest.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willBuildTotalPriceForAllCashReceiptProductsWhenDiscountCardIsPresent() {
        //given
        TotalPrice expected = TotalPrice.builder()
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
        TotalPrice actual = underTest.buildTotalPriceForAllCashReceiptProducts(cashReceiptProducts, discountCard);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willBuildCashReceiptProductsWithoutDiscountByProductIdAndQuantity() {
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
    void willBuildCashReceiptProductsWithDiscountByProductIdAndQuantity() {
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