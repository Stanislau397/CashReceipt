package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTotalPrice")
@With
@EqualsAndHashCode
@ToString
public class TotalPriceTestBuilder implements TestBuilder<TotalPrice> {

    private BigDecimal itemTotal = BigDecimal.valueOf(100);
    private BigDecimal discount = BigDecimal.valueOf(10);
    private BigDecimal subtotal = BigDecimal.valueOf(90);

    @Override
    public TotalPrice build() {
        return TotalPrice.builder()
                .itemTotal(itemTotal)
                .discount(discount)
                .subtotal(subtotal)
                .build();
    }
}
