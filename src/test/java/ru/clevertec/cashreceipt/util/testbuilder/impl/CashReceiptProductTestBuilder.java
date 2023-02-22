package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.CashReceiptProduct;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCashReceiptProduct")
@With
@EqualsAndHashCode
@ToString
public class CashReceiptProductTestBuilder implements TestBuilder<CashReceiptProduct> {

    private Integer quantity = 2;
    private Product product = ProductTestBuilder
            .aProduct()
            .build();
    private TotalPrice totalPrice = TotalPriceTestBuilder
            .aTotalPrice()
            .build();

    @Override
    public CashReceiptProduct build() {
        return CashReceiptProduct.builder()
                .product(product)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }
}
