package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;

import java.util.Optional;
import java.util.stream.Stream;

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

        static Stream<String> validCardIdArgumentsProvider() {
            return Stream.of("1", "20");
        }

        static Stream<String> invalidCardIdArgumentsProvider() {
            return Stream.of("1-1", "abc");
        }

        @ParameterizedTest
        @MethodSource("validCardIdArgumentsProvider")
        void checkFindDiscountCardByIdShouldReturnDiscountCard(String cardId) {
            //given
            DiscountCard expectedDiscountCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountCardId(Long.parseLong(cardId))
                    .build();

            //when
            when(discountCardRepository.selectById(Long.parseLong(cardId)))
                    .thenReturn(Optional.of(expectedDiscountCard));
            DiscountCard actualDiscountCard = underTest.findDiscountCardById(cardId);

            //then
            assertThat(actualDiscountCard).isEqualTo(expectedDiscountCard);
        }

        @ParameterizedTest
        @MethodSource("validCardIdArgumentsProvider")
        void checkFindDiscountCardByIdShouldThrowEntityNotFoundException(String cardId) {
            assertThatThrownBy(() -> underTest.findDiscountCardById(cardId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, Long.parseLong(cardId)));
        }

        @ParameterizedTest
        @MethodSource("invalidCardIdArgumentsProvider")
        void checkFindDiscountCardByIdShouldThrowInvalidInputException(String cardId) {
            assertThatThrownBy(() -> underTest.findDiscountCardById(cardId))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessage(String.format(GIVEN_ID_IS_NOT_VALID, cardId));
        }
    }
}