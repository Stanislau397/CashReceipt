package ru.clevertec.cashreceipt.util.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.util.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
@EqualsAndHashCode
@ToString
public class ProductTestBuilder implements TestBuilder<Product> {

    private Long productId = 1L;
    private String name = "Milk";
    private BigDecimal price = BigDecimal.valueOf(1.25);
    private Boolean promotional = false;

    @Override
    public Product build() {
        final Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setPrice(price);
        product.setPromotional(promotional);
        return product;
    }
}
