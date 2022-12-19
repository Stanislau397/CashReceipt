package ru.clevertec.cashier.service;

import ru.clevertec.cashier.entity.DiscountCard;

public interface DiscountCardService {

    DiscountCard findDiscountCardById(String id);
}
