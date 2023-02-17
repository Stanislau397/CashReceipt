package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.DiscountCard;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DiscountCardRepositoryTest {

    @Autowired
    private DiscountCardRepository underTest;

    @Nested
    class SelectDiscountCardByIdTest {

        @ParameterizedTest
        @CsvSource(value = {
                "1, 10",
                "2, 5",
                "3, 15"
        })
        void willSelectDiscountCardById(Long cardId, Integer discountPercent) {
            //given
            DiscountCard expectedDiscountCard = DiscountCard.builder()
                    .discountCardId(cardId)
                    .discountPercent(discountPercent)
                    .build();
            underTest.save(expectedDiscountCard);

            //when
            Optional<DiscountCard> actualDiscountCard = underTest.selectById(cardId);

            //then
            assertThat(actualDiscountCard).isEqualTo(Optional.of(expectedDiscountCard));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1", "2", "3"
        })
        void willNotSelectDiscountCardById(Long discountCardId) {
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