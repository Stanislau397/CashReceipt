package ru.clevertec.cashreceipt.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.impl.DiscountCardRepositoryImpl;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;

import java.util.Optional;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DiscountCardRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    private DiscountCardRepository discountCardRepository;

    @BeforeEach
    void setUp() {
        discountCardRepository = new DiscountCardRepositoryImpl(entityManager);
    }

    @AfterEach
    void tearDown() {
        discountCardRepository = null;
    }

    @Test
    void checkSaveShouldReturnDiscountCard() {
        DiscountCard expectedDiscountCard = DiscountCardTestBuilder.aDiscountCard().build();

        DiscountCard actualDiscountCard = discountCardRepository.save(expectedDiscountCard);

        assertThat(actualDiscountCard).isEqualTo(expectedDiscountCard);
    }

    @Test
    void checkUpdateShouldReturnNewDiscountCard() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

        discountCardRepository.save(discountCard);

        DiscountCard updatedDiscountCard = DiscountCardTestBuilder.aDiscountCard()
                .withDiscountPercent(25)
                .build();

        DiscountCard actualDiscountCard = discountCardRepository.update(updatedDiscountCard);

        assertThat(actualDiscountCard).isNotEqualTo(discountCard);
    }

    @Test
    void checkDeleteShouldReturnEmptyDiscountCard() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

        DiscountCard savedCard = discountCardRepository.save(discountCard);
        discountCardRepository.delete(savedCard);

        Optional<DiscountCard> actualDiscountCard = discountCardRepository.selectById(savedCard.getDiscountCardId());

        assertThat(actualDiscountCard).isEmpty();
    }

    @Nested
    class SelectDiscountCardByIdTest {

        static LongStream cardIdArgumentProvider() {
            return LongStream.of(1, 2, 3);
        }

        @Test
        void checkSelectDiscountCardByIdShouldReturnDiscountCard() {
            DiscountCard expectedDiscountCard = DiscountCardTestBuilder.aDiscountCard()
                    .build();
            discountCardRepository.save(expectedDiscountCard);

            Long discountCardId = expectedDiscountCard.getDiscountCardId();
            Optional<DiscountCard> actualDiscountCard = discountCardRepository.selectById(discountCardId);

            assertThat(actualDiscountCard).isEqualTo(Optional.of(expectedDiscountCard));
        }

        @ParameterizedTest
        @MethodSource("cardIdArgumentProvider")
        void checkSelectDiscountCardByIdShouldNotReturnDiscountCard(Long cardId) {
            Optional<DiscountCard> actualDiscountCard = discountCardRepository.selectById(cardId);

            assertThat(actualDiscountCard).isEmpty();
        }
    }
}