package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.util.impl.DiscountCardTestBuilder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DiscountCardRepositoryTest {

    @Autowired
    private DiscountCardRepository underTest;

    @Nested
    class SelectDiscountCardByIdTest {

        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3})
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
        @ValueSource(longs = {1, 2, 3})
        void checkSelectDiscountCardByIdShouldNotReturnDiscountCard(Long discountCardId) {
            //when
            Optional<DiscountCard> expectedCard = underTest.selectById(discountCardId);

            //then
            assertThat(expectedCard).isEmpty();
        }
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
}