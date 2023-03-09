package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.TotalPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {

    Product addProduct(Product product);

    void removeProductById(Long productId);

    Product updateProduct(Product product);

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
