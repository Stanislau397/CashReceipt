package ru.clevertec.cashreceipt.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class LRUCacheTest {

    private static final int CAPACITY = 2;
    private LRUCache<Long, DiscountCard> lruCache;

    @BeforeEach
    void setUp() {
        lruCache = Mockito.spy(new LRUCache<>(CAPACITY));
    }

    @Nested
    class PutTest {

        @Test
        void checkPutShouldCaptureKeyArgument() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long discountCardId = discountCard.getDiscountCardId();
            ArgumentCaptor<Long> keyCaptor = ArgumentCaptor.forClass(Long.class);

            doNothing().when(lruCache).put(
                    keyCaptor.capture(),
                    any(DiscountCard.class)
            );

            lruCache.put(discountCardId, discountCard);

            assertThat(keyCaptor.getValue()).isEqualTo(discountCardId);
        }

        @Test
        void checkPutShouldCaptureValueArgument() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long discountCardId = discountCard.getDiscountCardId();
            ArgumentCaptor<DiscountCard> valueCaptor = ArgumentCaptor.forClass(DiscountCard.class);

            doNothing().when(lruCache).put(
                    any(Long.class),
                    valueCaptor.capture()
            );

            lruCache.put(discountCardId, discountCard);

            assertThat(valueCaptor.getValue()).isEqualTo(discountCard);
        }

        @Test
        void checkPutShouldAddOneElementToMap() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long discountCardId = discountCard.getDiscountCardId();

            lruCache.put(discountCardId, discountCard);

            assertThat(lruCache.getNodeMap().size()).isEqualTo(1);
        }

        @Test
        void checkPutShouldRemoveOneElementFromMap() {
            DiscountCard firstCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .build();
            DiscountCard secondCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountCardId(2L)
                    .build();
            DiscountCard thirdCard = DiscountCardTestBuilder
                    .aDiscountCard()
                    .withDiscountCardId(3L)
                    .build();

            lruCache.put(firstCard.getDiscountCardId(), firstCard);
            lruCache.put(secondCard.getDiscountCardId(), secondCard);
            lruCache.put(thirdCard.getDiscountCardId(), thirdCard);

            assertThat(lruCache.getNodeMap().size()).isEqualTo(2);
        }
    }

    @Nested
    class GetTest {

        @Test
        void checkGetShouldNotBeEmpty() {
            DiscountCard card = DiscountCardTestBuilder
                    .aDiscountCard()
                    .build();
            Long cardId = card.getDiscountCardId();
            lruCache.put(card.getDiscountCardId(), card);

            Optional<DiscountCard> actualCard = lruCache.get(cardId);

            assertThat(actualCard).isNotEmpty();
        }

        @Test
        void checkGetShouldBeEmpty() {
            DiscountCard card = DiscountCardTestBuilder
                    .aDiscountCard()
                    .build();
            Long cardId = card.getDiscountCardId();

            Optional<DiscountCard> actualCard = lruCache.get(cardId);

            assertThat(actualCard).isEmpty();
        }
    }

    @Nested
    class RemoveTest {

        @Test
        void checkShouldRemoveValueFromNodeMap() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long cardId = discountCard.getDiscountCardId();

            lruCache.put(cardId, discountCard);
            lruCache.remove(cardId);

            assertThat(lruCache.getNodeMap().size())
                    .isEqualTo(0);
        }

        @Test
        void checkShouldNotRemoveValueFromNodeMap() {
            DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
            Long cardId = discountCard.getDiscountCardId();

            lruCache.put(cardId, discountCard);
            lruCache.remove(2L);

            assertThat(lruCache.getNodeMap().size())
                    .isEqualTo(1);
        }
    }
}