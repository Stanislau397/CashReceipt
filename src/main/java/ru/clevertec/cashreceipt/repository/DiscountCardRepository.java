package ru.clevertec.cashreceipt.repository;

import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.DiscountCard;

import java.util.Optional;

@Repository
public interface DiscountCardRepository {

    DiscountCard save(DiscountCard discountCard);

    DiscountCard delete(DiscountCard discountCard);

    DiscountCard update(DiscountCard discountCard);

    Optional<DiscountCard> selectById(Long discountCardId);
}
