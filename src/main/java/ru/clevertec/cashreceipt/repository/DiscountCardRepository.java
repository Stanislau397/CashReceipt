package ru.clevertec.cashreceipt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.DiscountCard;

import java.util.Optional;

import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_DISCOUNT_CARD_BY_ID;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    @Query(SELECT_DISCOUNT_CARD_BY_ID)
    Optional<DiscountCard> selectById(Long discountCardId);
}
