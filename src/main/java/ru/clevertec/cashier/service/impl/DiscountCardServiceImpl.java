package ru.clevertec.cashier.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.cashier.entity.DiscountCard;
import ru.clevertec.cashier.exception.EntityNotFoundException;
import ru.clevertec.cashier.exception.InvalidInputException;
import ru.clevertec.cashier.repository.DiscountCardRepository;
import ru.clevertec.cashier.service.DiscountCardService;
import ru.clevertec.cashier.validator.EntityIdValidator;

import static ru.clevertec.cashier.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashier.exception.ExceptionMessage.GIVEN_ID_IS_NOT_VALID;

@Service
@AllArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    @Override
    public DiscountCard findDiscountCardById(String id) {
        if (!EntityIdValidator.isEntityIdValid(id)) {
            throw new InvalidInputException(String.format(GIVEN_ID_IS_NOT_VALID, id));
        }
        Long discountCardId = Long.parseLong(id);
        return discountCardRepository.selectById(discountCardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, discountCardId)
                ));
    }
}
