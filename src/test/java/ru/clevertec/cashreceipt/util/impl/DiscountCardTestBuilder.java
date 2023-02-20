package ru.clevertec.cashreceipt.util.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.util.TestBuilder;

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
        final DiscountCard discountCard = new DiscountCard();
        discountCard.setDiscountCardId(discountCardId);
        discountCard.setDiscountPercent(discountPercent);
        return discountCard;
    }
}
