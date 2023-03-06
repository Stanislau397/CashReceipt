package ru.clevertec.cashreceipt.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;
import ru.clevertec.cashreceipt.repository.proxy.ProxyDiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.validator.EntityIdValidator;

import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.GIVEN_ID_IS_NOT_VALID;

@Service
@AllArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final ProxyDiscountCardRepository proxyDiscountCardRepository;

    @Override
    public DiscountCard findDiscountCardById(String id) {
        if (!EntityIdValidator.isEntityIdValid(id)) {
            throw new InvalidInputException(String.format(GIVEN_ID_IS_NOT_VALID, id));
        }
        Long discountCardId = Long.parseLong(id);
        return proxyDiscountCardRepository.selectById(discountCardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, discountCardId)
                ));
    }
}
