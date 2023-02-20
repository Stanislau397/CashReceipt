package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.util.impl.DiscountCardTestBuilder;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DiscountCardRepositoryTest {

    @Autowired
    private DiscountCardRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Nested
    class SelectDiscountCardByIdTest {

        static LongStream cardIdArgumentProvider() {
            return LongStream.of(1, 2, 3);
        }

        @ParameterizedTest
        @MethodSource("cardIdArgumentProvider")
        void checkSelectDiscountCardByIdShouldReturnDiscountCard(Long cardId) {
            //given
            DiscountCard expectedDiscountCard = DiscountCardTestBuilder.aDiscountCard()
                    .withDiscountCardId(cardId)
                    .build();
            underTest.save(expectedDiscountCard);

            //when
            Optional<DiscountCard> actualDiscountCard = underTest.selectById(cardId);

            //then
            assertThat(actualDiscountCard).isEqualTo(Optional.of(expectedDiscountCard));
        }

        @ParameterizedTest
        @MethodSource("cardIdArgumentProvider")
        void checkSelectDiscountCardByIdShouldNotReturnDiscountCard(Long cardId) {
            //when
            Optional<DiscountCard> actualDiscountCard = underTest.selectById(cardId);

            //then
            assertThat(actualDiscountCard).isEmpty();
        }
    }
}