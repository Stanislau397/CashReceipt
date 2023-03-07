package ru.clevertec.cashreceipt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cashreceipt.entity.DiscountCard;

import java.util.Optional;

@Repository
public interface DiscountCardRepository {

    @Transactional
    DiscountCard save(DiscountCard discountCard);

    @Transactional
    DiscountCard delete(DiscountCard discountCard);

    @Transactional
    DiscountCard update(DiscountCard discountCard);

    Optional<DiscountCard> selectById(Long discountCardId);
}
