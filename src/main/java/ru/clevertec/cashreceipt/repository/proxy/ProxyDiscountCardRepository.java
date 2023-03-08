package ru.clevertec.cashreceipt.repository.proxy;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.impl.DiscountCardRepositoryImpl;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProxyDiscountCardRepository extends DiscountCardRepositoryImpl {

    private final Cache<DiscountCard> discountCardCache;

    @Override
    public DiscountCard save(DiscountCard discountCard) {
        DiscountCard savedCard = super.save(discountCard);
        Long productId = savedCard.getDiscountCardId();
        if (discountCardCache.get(productId).isEmpty()) {
            discountCardCache.put(productId, savedCard);
        }
        return savedCard;
    }

    @Override
    public DiscountCard delete(DiscountCard discountCard) {
        DiscountCard deletedCard = super.delete(discountCard);
        Long cardId = deletedCard.getDiscountCardId();
        discountCardCache.remove(cardId);
        return deletedCard;
    }

    @Override
    public DiscountCard update(DiscountCard discountCard) {
        DiscountCard updatedDiscountCard = super.update(discountCard);
        Long discountCardId = updatedDiscountCard.getDiscountCardId();
        discountCardCache.put(discountCardId, updatedDiscountCard);
        return updatedDiscountCard;
    }

    @Override
    public Optional<DiscountCard> selectById(Long discountCardId) {
        Optional<DiscountCard> discountCardFromCache = discountCardCache.get(discountCardId);
        if (discountCardFromCache.isPresent()) {
            return discountCardFromCache;
        }
        Optional<DiscountCard> discountCardFromRepository = super.selectById(discountCardId);
        if (discountCardFromRepository.isPresent()) {
            DiscountCard discountCard = discountCardFromRepository.get();
            discountCardCache.put(discountCardId, discountCard);
        }
        return discountCardFromRepository;
    }
}
