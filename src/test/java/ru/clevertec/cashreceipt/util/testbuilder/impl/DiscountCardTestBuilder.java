package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDiscountCard")
@With
@EqualsAndHashCode
@ToString
public class DiscountCardTestBuilder implements TestBuilder<DiscountCard> {

    private Long discountCardId = 1L;
    private Integer discountPercent = 5;

    @Override
    public DiscountCard build() {
        return DiscountCard.builder()
                .discountCardId(discountCardId)
                .discountPercent(discountPercent)
                .build();
    }
}
