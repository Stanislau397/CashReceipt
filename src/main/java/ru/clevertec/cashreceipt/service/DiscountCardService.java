package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.entity.DiscountCard;

public interface DiscountCardService {

    DiscountCard findDiscountCardById(String id);
}
