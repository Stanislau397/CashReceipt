package ru.clevertec.cashier.service;

import ru.clevertec.cashier.entity.CashReceiptProduct;
import ru.clevertec.cashier.entity.DiscountCard;
import ru.clevertec.cashier.entity.Product;
import ru.clevertec.cashier.entity.TotalPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {

    Product findProductById(Long productId);

    List<CashReceiptProduct> buildCashReceiptProductsByProductIdAndQuantity(Map<Long, Integer> productIdAndQuantityMap);

    TotalPrice buildTotalPriceForAllCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts, DiscountCard discountCard);

    TotalPrice buildTotalPriceForSingleCashReceiptProduct(BigDecimal itemTotal, BigDecimal discount, BigDecimal subtotal);

    BigDecimal calculateProductPriceBasedOnQuantity(Integer quantity, BigDecimal productPrice);

    BigDecimal calculateDiscountForSingleProduct(BigDecimal totalPrice, Integer percent);

    BigDecimal calculateProductPriceWithDiscount(BigDecimal totalPrice, BigDecimal discountAmount);

    BigDecimal calculateAllProductsPriceWithDiscount(BigDecimal totalPriceForProducts, BigDecimal discountForAllProducts);

    BigDecimal calculateDiscountForAllProducts(BigDecimal itemsTotal, DiscountCard discountCard);

    BigDecimal calculatePriceForAllCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts);
}
