package ru.clevertec.cashreceipt.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.clevertec.cashreceipt.entity.DiscountCard;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DiscountCardRepositoryTest {

    @Autowired
    private DiscountCardRepository underTest;

    @Test
    void willSelectDiscountCardById() {
        //given
        Long discountCardId = 1L;
        DiscountCard discountCard = DiscountCard.builder()
                .discountCardId(discountCardId)
                .discountPercent(10)
                .build();
        underTest.save(discountCard);
        //when
        Optional<DiscountCard> expectedCard = underTest.selectById(discountCardId);
        //then
        assertThat(expectedCard).isEqualTo(Optional.of(discountCard));
    }

    @Test
    void willNotSelectDiscountCardById() {
        //given
        Long discountCardId = 2L;
        //when
        Optional<DiscountCard> expectedCard = underTest.selectById(discountCardId);
        //then
        assertThat(expectedCard).isEqualTo(Optional.empty());
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }
}