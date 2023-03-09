package ru.clevertec.cashreceipt.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.exception.EntityAlreadyExistsException;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.repository.proxy.ProxyProductRepository;
import ru.clevertec.cashreceipt.service.ProductService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.clevertec.cashreceipt.exception.ExceptionMessage.PRODUCT_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.PRODUCT_BY_GIVEN_NAME_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Integer DISCOUNT_PERCENT = 10;
    private static final Integer FIVE_PRODUCTS = 5;
    private static final Integer ONE_HUNDRED = 100;
    private final ProxyProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        String productName = product.getName();
        if (productRepository.selectByName(productName).isPresent()) {
            throw new EntityAlreadyExistsException(
                    String.format(PRODUCT_BY_GIVEN_NAME_ALREADY_EXISTS, productName)
            );
        }
        return productRepository.save(product);
    }

    @Override
    public void removeProductById(Long productId) {
        if (productRepository.selectById(productId).isEmpty()) {
            throw new EntityNotFoundException(
                    String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, productId)
            );
        }
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Product product) {
        if (productRepository.selectProduct(product).isEmpty()) {
            throw new EntityNotFoundException(
                    String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, product.getProductId())
            );
        }
        return productRepository.update(product);
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.selectById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(PRODUCT_BY_GIVEN_ID_NOT_FOUND, productId)
                ));
    }

    @Override
    public List<CashReceiptProduct> buildCashReceiptProductsByProductIdAndQuantity(Map<Long, Integer> productIdAndQuantityMap) {
        List<CashReceiptProduct> cashReceiptProducts = new ArrayList<>();
        productIdAndQuantityMap.forEach((productId, quantity) -> {
            Product product = findProductById(productId);
            Boolean isProductPromotional = product.getPromotional();
            BigDecimal itemTotal = calculateProductPriceBasedOnQuantity(quantity, product.getPrice());
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal priceIncludingDiscount = BigDecimal.ZERO;
            if (quantity > FIVE_PRODUCTS && isProductPromotional) {
                discountAmount = calculateDiscountForSingleProduct(itemTotal, DISCOUNT_PERCENT);
                priceIncludingDiscount = calculateProductPriceWithDiscount(itemTotal, discountAmount);
            }
            TotalPrice totalPrice = buildTotalPriceForSingleCashReceiptProduct(itemTotal, discountAmount, priceIncludingDiscount);
            CashReceiptProduct cashReceiptProduct = CashReceiptProduct.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .build();
            cashReceiptProducts.add(cashReceiptProduct);
        });
        return cashReceiptProducts;
    }

    @Override
    public TotalPrice buildTotalPriceForAllCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts, DiscountCard discountCard) {
        BigDecimal itemsTotal = calculatePriceForAllCashReceiptProducts(cashReceiptProducts);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal subtotal = BigDecimal.ZERO;
        if (discountCard.getDiscountCardId() != null) {
            discount = calculateDiscountForAllProducts(itemsTotal, discountCard);
            subtotal = calculateAllProductsPriceWithDiscount(itemsTotal, discount);
            TotalPrice totalPriceWithDiscountCard = TotalPrice.builder()
                    .itemTotal(itemsTotal)
                    .discount(discount)
                    .subtotal(subtotal)
                    .build();
            return totalPriceWithDiscountCard;
        }
        TotalPrice totalPriceWithoutDiscountCard = TotalPrice.builder()
                .itemTotal(itemsTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();
        return totalPriceWithoutDiscountCard;
    }

    @Override
    public TotalPrice buildTotalPriceForSingleCashReceiptProduct(BigDecimal itemTotal, BigDecimal discount, BigDecimal subtotal) {
        TotalPrice totalPrice = TotalPrice.builder()
                .itemTotal(itemTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();
        return totalPrice;
    }

    @Override
    public BigDecimal calculatePriceForAllCashReceiptProducts(List<CashReceiptProduct> cashReceiptProducts) {
        BigDecimal totalPriceForProducts = BigDecimal.ZERO;
        for (CashReceiptProduct cashReceiptProduct : cashReceiptProducts) {
            TotalPrice totalPrice = cashReceiptProduct.getTotalPrice();
            BigDecimal subtotalPrice = totalPrice.getSubtotal();
            BigDecimal productPrice = totalPrice.getItemTotal();
            if (!subtotalPrice.equals(BigDecimal.ZERO)) {
                totalPriceForProducts = totalPriceForProducts.add(subtotalPrice);
            } else {
                totalPriceForProducts = totalPriceForProducts.add(productPrice);
            }
        }
        return totalPriceForProducts;
    }

    @Override
    public BigDecimal calculateProductPriceBasedOnQuantity(Integer quantity, BigDecimal productPrice) {
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public BigDecimal calculateDiscountForSingleProduct(BigDecimal totalPrice, Integer percent) {
        return totalPrice.multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(ONE_HUNDRED), RoundingMode.CEILING);
    }

    @Override
    public BigDecimal calculateProductPriceWithDiscount(BigDecimal totalPrice, BigDecimal discountAmount) {
        return totalPrice.subtract(discountAmount);
    }

    @Override
    public BigDecimal calculateDiscountForAllProducts(BigDecimal productsTotalPrice, DiscountCard discountCard) {
        Integer discountPercent = discountCard.getDiscountPercent();
        return productsTotalPrice.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(ONE_HUNDRED), RoundingMode.CEILING);
    }

    @Override
    public BigDecimal calculateAllProductsPriceWithDiscount(BigDecimal totalPriceForProducts, BigDecimal discountForAllProducts) {
        return totalPriceForProducts.subtract(discountForAllProducts);
    }
}
