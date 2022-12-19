package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.GIVEN_ID_IS_NOT_VALID;

class DiscountCardServiceImplTest {

    @Mock
    private DiscountCardRepository discountCardRepository;
    private DiscountCardService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new DiscountCardServiceImpl(discountCardRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void willFindDiscountCardById() {
        //given
        Long cardId = 1L;
        DiscountCard discountCard = DiscountCard.builder()
                .discountCardId(cardId)
                .discountPercent(10)
                .build();
        //when
        when(discountCardRepository.selectById(cardId)).thenReturn(Optional.of(discountCard));
        //then
        DiscountCard expectedCard = underTest.findDiscountCardById(String.valueOf(cardId));
        assertThat(expectedCard).isEqualTo(discountCard);
    }

    @Test
    void findDiscountCardByIdWillThrowEntityNotFoundException() {
        //given
        Long cardId = 1L;
        //when
        when(discountCardRepository.selectById(cardId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.findDiscountCardById(String.valueOf(cardId)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, cardId));
    }

    @Test
    void findDiscountCardByIdWillThrowInvalidInputException() {
        //given
        String invalidId = "123asd";
        //when
        when(discountCardRepository.selectById(1L)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.findDiscountCardById(invalidId))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(String.format(GIVEN_ID_IS_NOT_VALID, invalidId));
    }
}