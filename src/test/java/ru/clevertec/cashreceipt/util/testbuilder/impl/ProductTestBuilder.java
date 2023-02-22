package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

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
        return Product.builder()
                .productId(productId)
                .name(name)
                .promotional(promotional)
                .price(price)
                .build();
    }
}
