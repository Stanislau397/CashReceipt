package ru.clevertec.cashreceipt.repository.proxy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;

import java.util.Optional;

@Repository
public class ProxyDiscountCardRepository implements DiscountCardRepository {

    private final DiscountCardRepository discountCardRepository;

    public ProxyDiscountCardRepository(@Qualifier("createDiscountCardRepository") DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public DiscountCard save(DiscountCard discountCard) {
        return discountCardRepository.save(discountCard);
    }

    @Override
    public void deleteById(Long discountCardId) {
        discountCardRepository.deleteById(discountCardId);
    }

    @Override
    public DiscountCard update(DiscountCard discountCard) {
        return discountCardRepository.update(discountCard);
    }

    @Override
    public DiscountCard selectById(Long discountCardId) {
        return discountCardRepository.selectById(discountCardId);
    }
}
