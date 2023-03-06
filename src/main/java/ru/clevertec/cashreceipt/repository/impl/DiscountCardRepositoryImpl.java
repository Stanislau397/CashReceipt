package ru.clevertec.cashreceipt.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;

import java.util.Optional;

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
    public DiscountCard delete(DiscountCard discountCard) {
        entityManager.remove(discountCard);
        return discountCard;
    }

    @Override
    public DiscountCard update(DiscountCard discountCard) {
        return entityManager.merge(discountCard);
    }

    @Override
    public Optional<DiscountCard> selectById(Long discountCardId) {
        return entityManager
                .createQuery("SELECT dc FROM DiscountCard dc WHERE dc.discountCardId = ?1", DiscountCard.class)
                .setParameter(1, discountCardId)
                .getResultStream()
                .findFirst();
    }
}
