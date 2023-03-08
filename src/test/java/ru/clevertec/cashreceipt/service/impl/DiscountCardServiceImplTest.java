package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.exception.EntityAlreadyExistsException;
import ru.clevertec.cashreceipt.exception.EntityNotFoundException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;
import ru.clevertec.cashreceipt.repository.proxy.ProxyDiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_ALREADY_EXISTS;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND;
import static ru.clevertec.cashreceipt.exception.ExceptionMessage.GIVEN_ID_IS_NOT_VALID;

class DiscountCardServiceImplTest {

    @Mock
    private ProxyDiscountCardRepository proxyDiscountCardRepository;
    private DiscountCardService discountCardService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        discountCardService = new DiscountCardServiceImpl(proxyDiscountCardRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Nested
    class AddDiscountCardTest {

        @Test
        void checkShouldAddDiscountCard() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

            doReturn(discountCard).when(proxyDiscountCardRepository)
                    .save(discountCard);

            DiscountCard actualCard = discountCardService.addDiscountCard(discountCard);

            assertThat(actualCard).isEqualTo(discountCard);
        }

        @Test
        void checkAddDiscountCardShouldThrowEntityAlreadyExistsException() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

            doReturn(Optional.of(discountCard))
                    .when(proxyDiscountCardRepository)
                    .selectById(discountCard.getDiscountCardId());

            assertThatThrownBy(() -> discountCardService.addDiscountCard(discountCard))
                    .hasMessage(String.format(DISCOUNT_CARD_BY_GIVEN_ID_ALREADY_EXISTS, discountCard.getDiscountCardId()))
                    .isInstanceOf(EntityAlreadyExistsException.class);
        }
    }

    @Nested
    class UpdateDiscountCardTest {

        @Test
        void checkShouldUpdateDiscountCard() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            DiscountCard newDiscountCard = DiscountCardTestBuilder.aDiscountCard()
                    .withDiscountPercent(12)
                    .build();

            Long cardId = discountCard.getDiscountCardId();

            doReturn(Optional.of(discountCard))
                    .when(proxyDiscountCardRepository)
                    .selectById(cardId);

            discountCardService.updateDiscountCard(newDiscountCard);

           verify(proxyDiscountCardRepository).update(newDiscountCard);
        }

        @Test
        void checkUpdateDiscountCardShouldThrowEntityNotFoundException() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long cardId = 1L;

            doReturn(Optional.empty())
                    .when(proxyDiscountCardRepository).selectById(cardId);

            assertThatThrownBy(() -> discountCardService.updateDiscountCard(discountCard))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, cardId);
        }
    }

    @Nested
    class RemoveDiscountCardByIdTest {

        @Test
        void checkShouldRemoveDiscountCardById() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long cardId = discountCard.getDiscountCardId();

            doReturn(Optional.of(discountCard))
                    .when(proxyDiscountCardRepository)
                    .selectById(cardId);

            discountCardService.removeDiscountCardById(cardId);

            verify(proxyDiscountCardRepository).deleteById(cardId);
        }

        @Test
        void checkRemoveDiscountCardByIdShouldThrowEntityNotFoundException() {
            Long cardId = 1L;

            doReturn(Optional.empty())
                    .when(proxyDiscountCardRepository).selectById(cardId);

            assertThatThrownBy(() -> discountCardService.removeDiscountCardById(cardId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, cardId);
        }
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
            DiscountCard expectedDiscountCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountCardId(Long.parseLong(cardId))
                    .build();

            doReturn(Optional.of(expectedDiscountCard))
                    .when(proxyDiscountCardRepository).selectById(Long.parseLong(cardId));

            DiscountCard actualDiscountCard = discountCardService.findDiscountCardById(cardId);

            assertThat(actualDiscountCard).isEqualTo(expectedDiscountCard);
        }

        @ParameterizedTest
        @MethodSource("validCardIdArgumentsProvider")
        void checkFindDiscountCardByIdShouldThrowEntityNotFoundException(String cardId) {
            assertThatThrownBy(() -> discountCardService.findDiscountCardById(cardId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format(DISCOUNT_CARD_BY_GIVEN_ID_NOT_FOUND, Long.parseLong(cardId)));
        }

        @ParameterizedTest
        @MethodSource("invalidCardIdArgumentsProvider")
        void checkFindDiscountCardByIdShouldThrowInvalidInputException(String cardId) {
            assertThatThrownBy(() -> discountCardService.findDiscountCardById(cardId))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessage(String.format(GIVEN_ID_IS_NOT_VALID, cardId));
        }
    }
}