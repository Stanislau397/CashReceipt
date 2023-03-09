package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.entity.DiscountCard;

public interface DiscountCardService {

    DiscountCard addDiscountCard(DiscountCard discountCard);

    DiscountCard updateDiscountCard(DiscountCard discountCard);

    void removeDiscountCardById(Long discountCardId);

    DiscountCard findDiscountCardById(String id);
}
