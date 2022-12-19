package ru.clevertec.cashier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashier.entity.DiscountCard;

import java.util.Optional;

import static ru.clevertec.cashier.repository.HqlQuery.SELECT_DISCOUNT_CARD_BY_ID;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    @Query(SELECT_DISCOUNT_CARD_BY_ID)
    Optional<DiscountCard> selectById(Long discountCardId);
}
