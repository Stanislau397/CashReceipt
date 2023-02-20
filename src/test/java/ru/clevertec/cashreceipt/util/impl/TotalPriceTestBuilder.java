package ru.clevertec.cashreceipt.util.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.TotalPrice;
import ru.clevertec.cashreceipt.util.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTotalPrice")
@With
@EqualsAndHashCode
@ToString
public class TotalPriceTestBuilder implements TestBuilder<TotalPrice> {

    private BigDecimal itemTotal = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Override
    public TotalPrice build() {
        final TotalPrice totalPrice = new TotalPrice();
        totalPrice.setItemTotal(itemTotal);
        totalPrice.setDiscount(discount);
        totalPrice.setSubtotal(subtotal);
        return totalPrice;
    }
}
