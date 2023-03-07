package ru.clevertec.cashreceipt.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.service.DiscountCardService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @PostMapping("/addDiscountCard")
    public ResponseEntity<DiscountCard> addDiscountCard(@Valid DiscountCard discountCard) {
        DiscountCard savedCard = discountCardService.addDiscountCard(discountCard);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }
}
