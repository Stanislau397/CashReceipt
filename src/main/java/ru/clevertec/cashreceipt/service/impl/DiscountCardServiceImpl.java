package ru.clevertec.cashreceipt.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.exception.EntityAlreadyExistsException;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;
import ru.clevertec.cashreceipt.repository.proxy.ProxyDiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.validator.EntityIdValidator;

import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_ALREADY_EXISTS;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.GIVEN_ID_IS_NOT_VALID;

@Service
@AllArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final ProxyDiscountCardRepository discountCardRepository;

    @Override
    public DiscountCard addDiscountCard(DiscountCard discountCard) {
        Long cardId = discountCard.getDiscountCardId();
        if (discountCardRepository.selectById(cardId) != null) {
            throw new EntityAlreadyExistsException(
                    String.format(DISCOUNT_CARD_BY_GIVEN_ID_ALREADY_EXISTS, cardId)
            );
        }
        return discountCardRepository.save(discountCard);
    }

    @Override
    public DiscountCard updateDiscountCard(DiscountCard discountCard) {
        Long cardId = discountCard.getDiscountCardId();
        if (discountCardRepository.selectById(cardId) == null) {
            throw new EntityNotFoundException(
                    String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, cardId)
            );
        }
        return discountCardRepository.update(discountCard);
    }

    @Override
    public void removeDiscountCardById(Long discountCardId) {
        if (discountCardRepository.selectById(discountCardId) == null) {
            throw new EntityNotFoundException(
                    String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, discountCardId)
            );
        }
        discountCardRepository.deleteById(discountCardId);
    }

    @Override
    public DiscountCard findDiscountCardById(String id) {
        if (!EntityIdValidator.isEntityIdValid(id)) {
            throw new InvalidInputException(String.format(GIVEN_ID_IS_NOT_VALID, id));
        }
        Long discountCardId = Long.parseLong(id);
        DiscountCard foundCardById = discountCardRepository.selectById(discountCardId);
        if (foundCardById == null) {
            throw new EntityNotFoundException(
                    String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, discountCardId)
            );
        }
        return discountCardRepository.selectById(discountCardId);
    }
}
