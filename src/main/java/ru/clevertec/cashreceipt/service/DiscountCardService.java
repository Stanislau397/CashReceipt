package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.entity.DiscountCard;

public interface DiscountCardService {

    DiscountCard addDiscountCard(DiscountCard discountCard);

    DiscountCard updateDiscountCard(DiscountCard discountCard);

    DiscountCard removeDiscountCard(DiscountCard discountCard);

    DiscountCard findDiscountCardById(String id);
}
