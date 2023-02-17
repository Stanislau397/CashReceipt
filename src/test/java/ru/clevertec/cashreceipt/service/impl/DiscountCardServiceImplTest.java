package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @Nested
    class FindDiscountCardByIdTest {

        @ParameterizedTest
        @CsvSource(value = {
                "1, 10",
                "50, 5"
        })
        void checkFindDiscountCardByIdShouldReturnDiscountCard(Long cardId, Integer discountPercent) {
            //given
            DiscountCard expectedDiscountCard = DiscountCard.builder()
                    .discountCardId(cardId)
                    .discountPercent(discountPercent)
                    .build();

            //when
            when(discountCardRepository.selectById(cardId))
                    .thenReturn(Optional.of(expectedDiscountCard));
            DiscountCard actualDiscountCard = underTest.findDiscountCardById(String.valueOf(cardId));

            //then
            assertThat(actualDiscountCard).isEqualTo(expectedDiscountCard);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1",
                "2"
        })
        void checkFindDiscountCardByIdShouldThrowEntityNotFoundException(Long cardId) {
            //when
            when(discountCardRepository.selectById(cardId))
                    .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> underTest.findDiscountCardById(String.valueOf(cardId)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, cardId));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1-1",
                "abc"
        })
        void checkFindDiscountCardByIdShouldThrowInvalidInputException(String cardId) {
            //when
            when(discountCardRepository.selectById(1L)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> underTest.findDiscountCardById(cardId))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessage(String.format(GIVEN_ID_IS_NOT_VALID, cardId));
        }
    }
}