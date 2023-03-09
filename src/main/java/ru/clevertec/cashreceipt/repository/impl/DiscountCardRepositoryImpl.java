package ru.clevertec.cashreceipt.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;

import java.util.Optional;

import static ru.clevertec.cashreceipt.repository.HqlQuery.DELETE_DISCOUNT_CARD_BY_ID;
import static ru.clevertec.cashreceipt.repository.HqlQuery.SELECT_DISCOUNT_CARD_BY_ID;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCardRepositoryImpl implements DiscountCardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DiscountCard save(DiscountCard discountCard) {
        return entityManager.merge(discountCard);
    }

    @Override
    public void deleteById(Long discountCardId) {
        entityManager.createQuery(DELETE_DISCOUNT_CARD_BY_ID)
                .setParameter(1, discountCardId)
                .executeUpdate();
    }

    @Override
    public DiscountCard update(DiscountCard discountCard) {
        return entityManager.merge(discountCard);
    }

    @Override
    public DiscountCard selectById(Long discountCardId) {
        return entityManager
                .createQuery(SELECT_DISCOUNT_CARD_BY_ID, DiscountCard.class)
                .setParameter(1, discountCardId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
