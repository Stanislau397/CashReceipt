package ru.clevertec.cashreceipt.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.service.DiscountCardService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("discountCard")
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountCard> addDiscountCard(@Valid @RequestBody DiscountCard discountCard) {
        DiscountCard savedCard = discountCardService.addDiscountCard(discountCard);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountCard> deleteDiscountCard(@Valid @RequestBody DiscountCard discountCard) {
        DiscountCard removedCard = discountCardService.removeDiscountCard(discountCard);
        return new ResponseEntity<>(removedCard, HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountCard> updateDiscountCard(@Valid @RequestBody DiscountCard discountCard) {
        DiscountCard updatedCard = discountCardService.updateDiscountCard(discountCard);
        return new ResponseEntity<>(updatedCard, HttpStatus.OK);
    }

    @GetMapping("/{discountCardId}")
    public ResponseEntity<DiscountCard> findDiscountCardById(@PathVariable String discountCardId) {
        DiscountCard discountCardById = discountCardService.findDiscountCardById(discountCardId);
        return new ResponseEntity<>(discountCardById, HttpStatus.OK);
    }
}
